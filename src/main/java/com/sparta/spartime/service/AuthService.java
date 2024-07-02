package com.sparta.spartime.service;

import com.sparta.spartime.dto.request.TokenReIssueRequestDto;
import com.sparta.spartime.dto.request.UserLoginRequestDto;
import com.sparta.spartime.dto.response.TokenResponseDto;
import com.sparta.spartime.entity.User;
import com.sparta.spartime.exception.BusinessException;
import com.sparta.spartime.exception.ErrorCode;
import com.sparta.spartime.security.principal.UserPrincipal;
import com.sparta.spartime.security.service.JwtService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.LockedException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final AuthenticationManager authenticationManager;
    private final JwtService jwtService;
    private final UserService userService;

    @Transactional
    public TokenResponseDto login(UserLoginRequestDto requestDto) {
        try {
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            requestDto.getEmail(),
                            requestDto.getPassword(),
                            null
                    )
            );

            UserPrincipal userPrincipal = (UserPrincipal) authentication.getPrincipal();
            User user = userService.findByEmail(userPrincipal.getUsername());

            String accessToken = jwtService.createAccessToken(user.getId(), user.getEmail(), user.getRole(), user.getStatus(), user.getNickname());
            String refreshToken = jwtService.createRefreshToken();

            user.addRefreshToken(refreshToken);

            return new TokenResponseDto(accessToken, refreshToken);
        } catch (LockedException e) {
            throw new BusinessException(ErrorCode.USER_BLOCKED);
        } catch (DisabledException e) {
            throw new BusinessException(ErrorCode.USER_INACTIVITY);
        } catch (AuthenticationException e) {
            throw new BusinessException(ErrorCode.FAIL_AUTHENTICATION);
        }
    }

    @Transactional
    public TokenResponseDto reIssueToken(TokenReIssueRequestDto requestDto) {
        String refreshToken = requestDto.getRefreshToken();

        if (!jwtService.isTokenValidate(refreshToken)) {
            throw new BusinessException(ErrorCode.INVALID_REFRESH_TOKEN);
        }

        User user = userService.findByRefreshToken(refreshToken);

        String newAccessToken = jwtService.createAccessToken(user.getId(), user.getEmail(), user.getRole(), user.getStatus(), user.getNickname());
        String newRefreshToken = jwtService.createRefreshToken();

        user.addRefreshToken(newRefreshToken);

        return new TokenResponseDto(newAccessToken, newRefreshToken);
    }

    /*
    1. 클라이언트 -> 서버에 반드시 AT를 전달해서 동작한다.
    2. AT가 만료되면 커스텀 상태 코드를 받고
    3. reissue API 를 요청한다. 이 때 RT도 같이 보낸다.
    4. RT를 받아서 DB에 있는 RT와 일치한지 조회한 후
    5. 일치하면 AT을 재발행한다.
    6. 클라이언트는 새로운 AT를 활용해서 1번 요청을 재요청 한다.
     */
}

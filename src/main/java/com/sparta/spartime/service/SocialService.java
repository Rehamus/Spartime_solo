package com.sparta.spartime.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.sparta.spartime.dto.request.UserLoginRequestDto;
import com.sparta.spartime.dto.request.UserSignupRequestDto;
import com.sparta.spartime.dto.response.KakaoUserInfoDto;
import com.sparta.spartime.dto.response.TokenResponseDto;
import com.sparta.spartime.entity.User;
import com.sparta.spartime.exception.BusinessException;
import com.sparta.spartime.exception.ErrorCode;
import com.sparta.spartime.repository.UserRepository;
import com.sparta.spartime.security.principal.UserPrincipal;
import com.sparta.spartime.security.service.JwtService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.RequestEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.LockedException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;
import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class SocialService {

    private final AuthenticationManager authenticationManager;
    private final PasswordEncoder passwordEncoder;
    private final UserRepository userRepository;
    private final RestTemplate restTemplate;
    private final JwtService jwtService;

    @Value("${SOCIAL_KAKAO_CLIENT_ID}")
    private String clientId;

    @Value("${SOCIAL_KAKAO_REDIRECT_URL}")
    private String redirectUri;


    @Transactional
    public TokenResponseDto login(String code) throws JsonProcessingException {

        String accessToken = getToken(code);

        KakaoUserInfoDto kakaoUserInfo = getKakaoUserInfo(accessToken);

        User User = registerKakaoUserIfNeeded(kakaoUserInfo);

        return socialLogin(User);
    }

    private String getToken(String code) throws JsonProcessingException {

        URI uri = UriComponentsBuilder
                .fromUriString("https://kauth.kakao.com")
                .path("/oauth/token")
                .encode()
                .build()
                .toUri();


        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-type", "application/x-www-form-urlencoded;charset=utf-8");


        MultiValueMap<String, String> body = new LinkedMultiValueMap<>();
        body.add("grant_type", "authorization_code");
        body.add("client_id", "03823b7d5644d2e6a1b88cb44e1afdaf");
        body.add("redirect_uri", "http://localhost:8080/api/users/login/kakao");
        body.add("code", code);

        RequestEntity<MultiValueMap<String, String>> requestEntity = RequestEntity
                .post(uri)
                .headers(headers)
                .body(body);

        ResponseEntity<String> response = restTemplate.exchange(
                requestEntity,
                String.class
        );

        JsonNode jsonNode = new ObjectMapper().readTree(response.getBody());
        return jsonNode.get("access_token").asText();
    }


    private KakaoUserInfoDto getKakaoUserInfo(String accessToken) throws JsonProcessingException {
        URI uri = UriComponentsBuilder
                .fromUriString("https://kapi.kakao.com")
                .path("/v2/user/me")
                .encode()
                .build()
                .toUri();

        HttpHeaders headers = new HttpHeaders();
        headers.add("Authorization", "Bearer " + accessToken);
        headers.add("Content-type", "application/x-www-form-urlencoded;charset=utf-8");

        RequestEntity<MultiValueMap<String, String>> requestEntity = RequestEntity
                .post(uri)
                .headers(headers)
                .body(new LinkedMultiValueMap<>());

        ResponseEntity<String> response = restTemplate.exchange(
                requestEntity,
                String.class
        );

        JsonNode jsonNode = new ObjectMapper().readTree(response.getBody());
        Long id = jsonNode.get("id").asLong();
        String nickname = jsonNode.get("properties").get("nickname").asText();
        String email = jsonNode.get("kakao_account").get("email").asText();
        return new KakaoUserInfoDto(id, nickname, email);
    }


    private User registerKakaoUserIfNeeded(KakaoUserInfoDto kakaoUserInfo) {
        Long SocialId = kakaoUserInfo.getId();
        String SocialEmail = kakaoUserInfo.getEmail();
        User getUser = userRepository.findByEmail(SocialEmail).orElse(null);

        if (getUser != null) {
            getUser.socialIdUpdate(SocialId);
            userRepository.save(getUser);
            return getUser;
        } else {
            String password = UUID.randomUUID().toString();
           User user = User.builder()
                    .email(kakaoUserInfo.getEmail())
                    .password(passwordEncoder.encode(password))
                    .nickname(kakaoUserInfo.getNickname())
                    .role(User.Role.USER)
                    .status(User.Status.ACTIVITY)
                    .socialId(SocialId)
                    .build();
            userRepository.save(user);
            return user;
        }
    }

    @Transactional
    public TokenResponseDto socialLogin(User user) {

        String accessToken = jwtService.createAccessToken(user.getId(), user.getEmail(), user.getRole(), user.getStatus(), user.getNickname());

        String refreshToken = jwtService.createRefreshToken();

        user.addRefreshToken(refreshToken);

        return new TokenResponseDto(accessToken, refreshToken);
    }

}
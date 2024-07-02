package com.sparta.spartime.web.controller;

import com.sparta.spartime.aop.envelope.Envelope;
import com.sparta.spartime.dto.request.TokenReIssueRequestDto;
import com.sparta.spartime.dto.request.UserLoginRequestDto;
import com.sparta.spartime.dto.response.TokenResponseDto;
import com.sparta.spartime.exception.BusinessException;
import com.sparta.spartime.exception.ErrorCode;
import com.sparta.spartime.service.AuthService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {
    private final AuthService authService;

    @Envelope("login 성공")
    @PostMapping("/login")
    public ResponseEntity<TokenResponseDto> login(@Valid @RequestBody UserLoginRequestDto requestDto) {
        return ResponseEntity.ok(authService.login(requestDto));
    }

    @PostMapping("/reissue")
    public ResponseEntity<TokenResponseDto> reIssueAccessToken(@Valid @RequestBody TokenReIssueRequestDto requestDto) {
        return ResponseEntity.ok(authService.reIssueToken(requestDto));
    }

    @GetMapping("/test")
    public ResponseEntity<?> test() {
        throw new BusinessException(ErrorCode.USER_NOT_FOUND);
    }
}

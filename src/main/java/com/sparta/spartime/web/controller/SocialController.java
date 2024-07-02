package com.sparta.spartime.web.controller;

import com.sparta.spartime.aop.envelope.Envelope;
import com.sparta.spartime.dto.request.UserLoginRequestDto;
import com.sparta.spartime.dto.response.TokenResponseDto;
import com.sparta.spartime.service.SocialService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class SocialController {

    private final SocialService socialService;


    @Envelope("Social login 성공")
    @GetMapping("/login/kakao")
    public ResponseEntity<TokenResponseDto> login(@RequestParam("code") String code) throws IOException {
        return ResponseEntity.ok(socialService.login(code));
    }

}

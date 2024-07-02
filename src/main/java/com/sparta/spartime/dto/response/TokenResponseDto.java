package com.sparta.spartime.dto.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class TokenResponseDto {
    private String accessToken;
    private String refreshToken;
}
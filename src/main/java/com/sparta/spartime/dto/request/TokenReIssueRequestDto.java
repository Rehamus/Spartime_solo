package com.sparta.spartime.dto.request;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;

@Getter
public class TokenReIssueRequestDto {
    @NotBlank
    private String refreshToken;
}

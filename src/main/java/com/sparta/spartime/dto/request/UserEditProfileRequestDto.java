package com.sparta.spartime.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.Getter;

@Getter
public class UserEditProfileRequestDto {
    @NotBlank(message = "비밀번호 입력은 필수 입니다.")
    private String password;
    @Pattern(regexp = "^(?=.*[a-zA-Z])(?=.*\\d)(?=.*[!@#$%^&*()_+\\-=\\[\\]{};':\"\\\\|,.<>/?]).{8,15}$",
            message = "비밀 번호 형식을 맞춰주세요")
    private String newPassword;
    private String newNickname;
    private String newIntro;
}

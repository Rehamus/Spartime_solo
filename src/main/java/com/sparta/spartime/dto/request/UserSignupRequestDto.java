package com.sparta.spartime.dto.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Pattern;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserSignupRequestDto {
    @Email
    private String email;
    //최소 8자 이상, 15자 이하이며 알파벳 대소문자(az, AZ), 숫자(0~9), 특수문자 사용
    // 예시 Secure#2024
    @Pattern(regexp = "^(?=.*[a-zA-Z])(?=.*\\d)(?=.*[!@#$%^&*()_+\\-=\\[\\]{};':\"\\\\|,.<>/?]).{8,15}$",
            message = "비밀 번호 형식을 맞춰주세요")
    private String password;

    private String nickname;

    private String intro;
}

package com.sparta.spartime.dto.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Pattern;
import lombok.Getter;

@Getter
public class UserLoginRequestDto {
    @Email
    private String email;
    @Pattern(regexp = "^(?=.*[a-zA-Z])(?=.*\\d)(?=.*[!@#$%^&*()_+\\-=\\[\\]{};':\"\\\\|,.<>/?]).{8,15}$",
            message = "비밀 번호 형식을 맞춰주세요")
    private String password;

    public UserLoginRequestDto(String email , String password){
        this.email = email;
        this.password = password;
    }
}

package com.sparta.spartime.security.exception;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sparta.spartime.dto.response.EnvelopeResponse;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;

import java.io.IOException;
import java.nio.charset.StandardCharsets;

@RequiredArgsConstructor
public class AuthenticationEntryPointImpl implements AuthenticationEntryPoint {
    private final ObjectMapper objectMapper;
    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException authException) throws IOException, ServletException {
        HttpStatus status = HttpStatus.FORBIDDEN;
        response.setCharacterEncoding(StandardCharsets.UTF_8.name());
        response.setStatus(status.value());
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        response.getWriter().write(
                objectMapper.writeValueAsString(
                        EnvelopeResponse.wrapError(
                                status,
                                "인증된 유저만 접근 가능합니다.",
                                request.getRequestURI()
                        ).getBody()
                )
        );
    }
}

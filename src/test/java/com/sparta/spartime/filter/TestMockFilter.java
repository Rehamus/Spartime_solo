package com.sparta.spartime.filter;

import jakarta.servlet.*;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import com.sparta.spartime.security.principal.UserPrincipal;

import java.io.IOException;

public class TestMockFilter implements Filter {

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        // 초기화 로직 (필요한 경우)
    }



    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication != null && authentication.getPrincipal() instanceof UserPrincipal) {
            UserPrincipal userPrincipal = (UserPrincipal) authentication.getPrincipal();
            // UserPrincipal 객체를 이용하여 필터 처리 로직을 수행할 수 있음
            // 예시: 사용자가 특정 권한을 가지고 있는지 확인하거나, 사용자 정보를 활용한 특정 작업 수행
            // 예시: 특정 요청에 대한 접근 제어 등
        }

        chain.doFilter(request, response);
    }

    @Override
    public void destroy() {
        // 필터 종료 시 처리할 로직 (필요한 경우)
    }
}

package com.sparta.spartime.security.filter;

import com.sparta.spartime.entity.User;
import com.sparta.spartime.security.principal.UserPrincipal;
import com.sparta.spartime.security.service.JwtService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@RequiredArgsConstructor
public class AuthenticationFilter extends OncePerRequestFilter {

    private final JwtService jwtService;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        String accessTokenValue = jwtService.getJwtFromHeader(request);

        if (!StringUtils.hasText(accessTokenValue) || !accessTokenValue.startsWith(JwtService.AUTH_SCHEME)) {
            filterChain.doFilter(request, response);
            return;
        }

        String accessToken = jwtService.substringToken(accessTokenValue);

        // TODO: 만료되면 custom status code 보내주기(499)
        if (!jwtService.isTokenValidate(accessToken)) {
            filterChain.doFilter(request, response);
            return;
        }

        User user = jwtService.getUserFromAccessToken(accessToken);
        UserPrincipal userPrincipal = new UserPrincipal(user);
        Authentication authentication = new UsernamePasswordAuthenticationToken(userPrincipal, null, null);
        SecurityContext context = SecurityContextHolder.createEmptyContext();
        context.setAuthentication(authentication);
        SecurityContextHolder.setContext(context);
        filterChain.doFilter(request, response);
    }
}

package com.sparta.spartime.security.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sparta.spartime.security.exception.AccessDeniedHandlerImpl;
import com.sparta.spartime.security.exception.AuthenticationEntryPointImpl;
import com.sparta.spartime.security.filter.AuthenticationFilter;
import com.sparta.spartime.security.service.JwtService;
import com.sparta.spartime.web.filter.transaction.TransactionFilter;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;

@Configuration
@RequiredArgsConstructor
public class SecurityConfig {
    private final ObjectMapper objectMapper;

    private final JwtService jwtService;

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration configuration) throws Exception {
        return configuration.getAuthenticationManager();
    }

    @Bean
    public AuthenticationFilter authenticationFilter(JwtService jwtService) {
        return new AuthenticationFilter(jwtService);
    }

    @Bean
    SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http.csrf(AbstractHttpConfigurer::disable);
        http.formLogin(AbstractHttpConfigurer::disable);
        http.httpBasic(AbstractHttpConfigurer::disable);
//        http.userDetailsService(userDetailsService);

        http.exceptionHandling(handler -> {
                    handler.accessDeniedHandler(new AccessDeniedHandlerImpl(objectMapper));
                    handler.authenticationEntryPoint(new AuthenticationEntryPointImpl(objectMapper));
                }
        );

        http.authorizeHttpRequests(requests -> requests
                        .requestMatchers("api/users").permitAll()
                        .requestMatchers(HttpMethod.GET, "api/posts/**").permitAll()
                        .requestMatchers(HttpMethod.POST, "/api/auth/login").permitAll()
                        .requestMatchers(HttpMethod.GET, "api/users/login/**").permitAll()
                        .requestMatchers(HttpMethod.POST, "/api/auth/reissue").permitAll()
                        .requestMatchers(HttpMethod.GET, "/api/admin/posts").permitAll()
                        .anyRequest().authenticated()
        );

        http.sessionManagement(sessionManagement -> sessionManagement
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
        );

        http.addFilterAt(new TransactionFilter(), BasicAuthenticationFilter.class);
        http.addFilterAt(authenticationFilter(jwtService), TransactionFilter.class);

        return http.build();
    }
}

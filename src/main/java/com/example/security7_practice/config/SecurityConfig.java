package com.example.security7_practice.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.access.hierarchicalroles.RoleHierarchy;
import org.springframework.security.access.hierarchicalroles.RoleHierarchyImpl;
import org.springframework.security.authorization.AuthorizationDecision;
import org.springframework.security.authorization.AuthorizationManager;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.access.intercept.RequestAuthorizationContext;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Value("${app.security.remember-me.key}")
    private String rememberMeKey;

    // 인터페이스 기반 설계를 위해 구현체 BBCryptPasswordEncoder가 아닌 인터페이스 PasswordEncoder를 빈으로 등록
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    // Role Hierarchy 설정
    @Bean
    public RoleHierarchy roleHierarchy() {
        return RoleHierarchyImpl.withRolePrefix("ROLE_")
                .role("ADMIN").implies("USER")
                .build();
    }

    // 시큐리티 필터 구획을 내 마음대로 커스텀
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {

        // CSRF 필터를 기본 enable 및 특정 경로는 disable
        http.csrf(csrf -> csrf
                .ignoringRequestMatchers("/logout"));

        // remember me 설정
        http.rememberMe(me -> me
                .key(rememberMeKey)
                .rememberMeParameter("remember-me")
                .tokenValiditySeconds(14 * 24 * 60 * 60));

        // 접근 경로별 인가 설정
        http.authorizeHttpRequests(auth -> auth
                .requestMatchers("/").permitAll() // 인덱스 페이지 열어주기
                .requestMatchers("/user/join").permitAll() // 회원가입 페이지 열어주기
                .requestMatchers("/user/login").permitAll() // 로그인 페이지 열어주기
                .requestMatchers("/user").hasRole("USER") // 유저 권한만 접근 가능한 페이지(Hierarchy 설정 또는 hasAnyRole로 관리자도 접근 가능)
                .requestMatchers("/admin").access(authorizationManager()) // 관리자 권한만 접근 가능한 페이지
                .anyRequest().denyAll()); // 위 경로 외에는 다 거부

        // 로그인 필터 설정
        http.formLogin(login -> login
                .loginProcessingUrl("/login")
                .loginPage("/user/login"));

        // 최종 빌드
        return http.build();
    }

    private AuthorizationManager<RequestAuthorizationContext> authorizationManager() {

        return (authentication, context) -> {

            boolean allowed =
                    authentication.get().getAuthorities().stream()
                            .anyMatch(a -> a.getAuthority().equals("ROLE_ADMIN"));

            // 지역 맞는지
            // 비즈니스 유저인지 개인 유저인지
            // 등.. 커스텀을 통해 조건 설정 가능

            return new AuthorizationDecision(allowed);
        };
    }
}

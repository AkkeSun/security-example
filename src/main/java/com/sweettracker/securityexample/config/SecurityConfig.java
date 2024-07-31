package com.sweettracker.securityexample.config;

import java.util.Collections;
import org.springframework.boot.autoconfigure.security.servlet.PathRequest;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;

/*
    인증을 위한 DB 설정을 따로 하지 않아도 됩니다.
 */
@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
            // --------------- 인증 정책 ---------------
            .formLogin(login -> {
                login.loginPage("/login-page")             // 커스텀 로그인 페이지 url
                    .loginProcessingUrl("/login-process")  // 로그인 프로세싱 url (default = /login)
                    .usernameParameter("username")         // 로그인 아이디 파라미터명 (default = username)
                    .passwordParameter("password")         // 로그인 패스워드 파라미터명 (default = password)
                    .defaultSuccessUrl("/")
                    .failureUrl("/login-page?loginFail=true");
            })

            // --------------- 인가 정책 ---------------
            // 순서 : 1. 전체허용, 2. 아래로 갈수록 권한이 넓어지도록 설정
            .authorizeHttpRequests(auth -> {
                auth.requestMatchers("/login-page").permitAll()
                    .requestMatchers("/error/access-denied").permitAll()
                    .requestMatchers("/user").hasRole("USER")
                    .requestMatchers("/admin").hasRole("ADMIN")
                    .anyRequest().authenticated();
            })

            //--------------- 인가 예외 처리 ---------------
            .exceptionHandling(e -> {
                e.accessDeniedHandler((request, response, accessDeniedException) ->
                    response.sendRedirect("/error/access-denied"));
            })

            //--------------- 로그아웃 설정 ---------------
            .logout(logout -> {
                logout.logoutUrl("/logout")
                    .logoutSuccessUrl("/login-page")
                    .deleteCookies("remember-me");
            })

            //--------------- csrf, cors 설정 ---------------
            .csrf(AbstractHttpConfigurer::disable)
            .cors(corsConfigurer -> corsConfigurer.configurationSource(corsConfigurationSource()))

        ;
        return http.build();
    }

    private CorsConfigurationSource corsConfigurationSource() {
        return request -> {
            CorsConfiguration config = new CorsConfiguration();
            config.setAllowedHeaders(Collections.singletonList("*"));
            config.setAllowedMethods(Collections.singletonList("*"));
            config.setAllowedOriginPatterns(Collections.singletonList("*"));
            config.setAllowCredentials(true);
            return config;
        };
    }


    // ------------ 정적파일 허용 ------------
    @Bean
    public WebSecurityCustomizer webSecurityCustomizer() {
        return (web) -> web.ignoring()
            .requestMatchers(PathRequest.toStaticResources().atCommonLocations());
    }
}
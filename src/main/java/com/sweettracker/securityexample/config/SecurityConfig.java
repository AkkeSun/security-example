package com.sweettracker.securityexample.config;

import com.sweettracker.securityexample.service.MemberService;
import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.autoconfigure.security.servlet.PathRequest;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.access.AccessDeniedHandler;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    private final MemberService userDetailsService;
    private final PasswordEncoder passwordEncoder;

    @Override
    protected void configure(HttpSecurity http) throws Exception {

        // --------------- 인증 정책 ---------------
        http
            .formLogin() // form 로그인 사용
            .loginPage("/login-page")              // 커스텀 로그인 페이지 url
            .loginProcessingUrl("/login-process")  // 로그인 프로세싱 url (default = /login)
            .usernameParameter("username")         // 로그인 아이디 파라미터명 (default = username)
            .passwordParameter("password")         // 로그인 패스워드 파라미터명 (default = password)
            .defaultSuccessUrl("/")
            .failureUrl("/login-page?loginFail=true")
        ;

        // --------------- 인가 정책 ---------------
        // 순서 : 1. 전체허용, 2. 아래로 갈수록 권한이 넓어지도록 설정
        http
            .authorizeRequests()
            .antMatchers("/login-page").permitAll()
            .antMatchers("/error/access-denied").permitAll()
            .antMatchers("/user").hasRole("USER")
            .antMatchers("/admin").hasRole("ADMIN")
            .antMatchers("/user-and-admin").access("hasRole('USER') or hasRole('ADMIN')")
            .anyRequest().authenticated()
        //  .antMatchers("/test/ip").hasIpAddress("127.0.0.1")
        //  .mvcMatchers(HttpMethod.GET, "shop/mvc").permitAll();
        ;

        //--------------- 인가 예외 처리 ---------------
        http
            .exceptionHandling()
            .accessDeniedHandler(new AccessDeniedHandler() {
                @Override
                public void handle(HttpServletRequest request, HttpServletResponse response,
                    org.springframework.security.access.AccessDeniedException accessDeniedException)
                    throws IOException, ServletException {
                    response.sendRedirect("/error/access-denied");

                }
            })
        ;

        //--------------- 로그아웃 설정 ---------------
        http
            .logout()
            .logoutUrl("/logout")
            .logoutSuccessUrl("/login-page")
            .deleteCookies("remember-me")
        ;

        //--------------- csrf, cors 설정 ---------------
        http
            .csrf().disable()
            .cors();
    }


    @Override
    // ============= DB로 사용자 정보 관리하기 위한 설정 =============
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(userDetailsService).passwordEncoder(passwordEncoder);
    }

    @Override
    // ============= 정적파일 허용 =============
    public void configure(WebSecurity web) {
        web.ignoring()
            .requestMatchers(PathRequest.toStaticResources().atCommonLocations());
    }
}
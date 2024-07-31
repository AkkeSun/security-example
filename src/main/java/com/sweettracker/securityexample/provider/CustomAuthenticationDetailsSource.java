package com.sweettracker.securityexample.provider;

import javax.servlet.http.HttpServletRequest;
import org.springframework.security.authentication.AuthenticationDetailsSource;
import org.springframework.security.web.authentication.WebAuthenticationDetails;
import org.springframework.stereotype.Component;

/*
	CustomWebAuthenticationDetails 를 Filter 에서 호출하기 위한 Bean
*/
@Component
public class CustomAuthenticationDetailsSource implements
    AuthenticationDetailsSource<HttpServletRequest, WebAuthenticationDetails> {

    @Override
    public WebAuthenticationDetails buildDetails(HttpServletRequest request) {
        return new CustomWebAuthenticationDetails(request);
    }
}

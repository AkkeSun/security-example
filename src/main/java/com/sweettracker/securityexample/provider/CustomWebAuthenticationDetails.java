package com.sweettracker.securityexample.provider;

import jakarta.servlet.http.HttpServletRequest;
import lombok.Getter;
import org.springframework.security.web.authentication.WebAuthenticationDetails;

/*
    사용자에게 추가로 받은 정보로 미인증 토큰(Authenticaion) 을 만들기 위한 설정
    위 정보는 Authenticaion.getDetails() 에 담기고 provider 에서 인증 처리를 할 때 사용된다
 */
@Getter
public class CustomWebAuthenticationDetails extends WebAuthenticationDetails {

    private String secretKey;

    public CustomWebAuthenticationDetails(HttpServletRequest request) {
        super(request);
        secretKey = request.getParameter("secretKey");
    }
}

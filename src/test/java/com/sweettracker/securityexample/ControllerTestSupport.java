package com.sweettracker.securityexample;

import com.sweettracker.securityexample.config.SecurityConfig;
import com.sweettracker.securityexample.service.MemberService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.web.servlet.MockMvc;

@WebMvcTest
@Import(SecurityConfig.class)
public class ControllerTestSupport {

    @Autowired
    protected MockMvc mockMvc;

    @MockBean
    private MemberService userDetailsService;

    @MockBean
    private PasswordEncoder passwordEncoder;
}

package com.sweettracker.securityexample.controller;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.redirectedUrl;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

import com.sweettracker.securityexample.ControllerTestSupport;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.ResultActions;

class WebControllerTest extends ControllerTestSupport {
    
    @Nested
    @DisplayName("[login-page] 로그인 페이지 이동 컨트롤러")
    class Describe_login_page {

        @Test
        @DisplayName("[success] 인증받지 않은 사용자가 로그인 페이지에 접근할 수 있는지 확인한다.")
        public void success() throws Exception {
            // given
            String uri = "/login-page";

            // when
            ResultActions perform = mockMvc.perform(get(uri));

            // then
            perform
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(view().name("login"));
        }

        @Test
        @DisplayName("[success] 인증받은 사용자가 로그인 페이지에 접근하는 경우 메인 페이지로 리다이렉트 되는지 확인한다.")
        @WithMockUser(username = "user", roles = "USER")
        public void success2() throws Exception {
            // given
            String uri = "/login-page";

            // when
            ResultActions perform = mockMvc.perform(get(uri));

            // then
            perform
                .andDo(print())
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/"));
        }
    }

    @Nested
    @DisplayName("[home] 메인 페이지 이동 컨트롤러")
    class Describe_home {

        @Test
        @DisplayName("[success] 인증받지 않은 사용자가 메인 페이지에 접근하는 경우 로그인 페이지로 리다이렉트 되는지 확인한다.")
        public void success() throws Exception {
            // given
            String uri = "/";

            // when
            ResultActions perform = mockMvc.perform(get(uri));

            // then
            perform
                .andDo(print())
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("http://localhost/login-page"));
        }

        @Test
        @DisplayName("[success] 인증받은 사용자가 메인 페이지에 접근할 수 있는지 확인한다.")
        @WithMockUser(username = "user", roles = "USER")
        public void success2() throws Exception {
            // given
            String uri = "/";

            // when
            ResultActions perform = mockMvc.perform(get(uri));

            // then
            perform
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(view().name("index"));
        }
    }

    @Nested
    @DisplayName("[accessDenied] 에러 페이지 이동 컨트롤러")
    class Describe_access_denied {

        @Test
        @DisplayName("[success] 인증받은 사용자가 에러페이지에 접근할 수 있는지 확인한다.")
        @WithMockUser(username = "user", roles = "USER")
        public void success() throws Exception {
            // given
            String uri = "/error/access-denied";

            // when
            ResultActions perform = mockMvc.perform(get(uri));

            // then
            perform
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(view().name("accessDenied"));
        }

        @Test
        @DisplayName("[success] 인증받지 않은 사용자가 에러페이지에 접근할 수 있는지 확인한다.")
        public void success2() throws Exception {
            // given
            String uri = "/error/access-denied";

            // when
            ResultActions perform = mockMvc.perform(get(uri));

            // then
            perform
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(view().name("accessDenied"));
        }
    }
}
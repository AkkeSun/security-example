package com.sweettracker.securityexample.controller;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.redirectedUrl;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.sweettracker.securityexample.ControllerTestSupport;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.ResultActions;

class ApiControllerTest extends ControllerTestSupport {

    @Nested
    @DisplayName("[user] 사용자 정보 조회 API")
    class Describe_user {

        @Test
        @DisplayName("[success] USER 권한을 가진 사용자가 인증 받았을 때 API 호출에 성공하는지 확인한다.")
        @WithMockUser(username = "user", roles = "USER")
        public void success() throws Exception {
            // given
            String uri = "/user";

            // when
            ResultActions perform = mockMvc.perform(get(uri));

            // then
            perform
                .andDo(print())
                .andExpect(status().isOk());
        }

        @Test
        @DisplayName("[success] USER 가 아닌 권한을 가진 사용자가 인증 받았을 때 에러 페이지로 리다이렉트되는지 확인한다.")
        @WithMockUser(username = "user", roles = "ADMIN")
        public void success2() throws Exception {
            // given
            String uri = "/user";

            // when
            ResultActions perform = mockMvc.perform(get(uri));

            // then
            perform
                .andDo(print())
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/error/access-denied"));
        }

        @Test
        @DisplayName("[success] 인증받지 않은 사용자가 API 를 호출할 때 로그인 페이지로 리다이렉트 되는지 확인한다. ")
        public void success3() throws Exception {
            // given
            String uri = "/user";

            // when
            ResultActions perform = mockMvc.perform(get(uri));

            // then
            perform
                .andDo(print())
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("http://localhost/login-page"));
        }
    }

    @Nested
    @DisplayName("[admin] 관리자 정보 조회 API")
    class Describe_admin {

        @Test
        @DisplayName("[success] ADMIN 권한을 가진 사용자가 인증 받았을 때 API 호출에 성공하는지 확인한다.")
        @WithMockUser(username = "user", roles = "ADMIN")
        public void success() throws Exception {
            // given
            String uri = "/admin";

            // when
            ResultActions perform = mockMvc.perform(get(uri));

            // then
            perform
                .andDo(print())
                .andExpect(status().isOk());
        }

        @Test
        @DisplayName("[success] ADMIN 이 아닌 권한을 가진 사용자가 인증 받았을 때 에러 페이지로 리다이렉트되는지 확인한다.")
        @WithMockUser(username = "user", roles = "USER")
        public void success2() throws Exception {
            // given
            String uri = "/admin";

            // when
            ResultActions perform = mockMvc.perform(get(uri));

            // then
            perform
                .andDo(print())
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/error/access-denied"));
        }

        @Test
        @DisplayName("[success] 인증받지 않은 사용자가 API 를 호출할 때 로그인 페이지로 리다이렉트 되는지 확인한다. ")
        public void success3() throws Exception {
            // given
            String uri = "/admin";

            // when
            ResultActions perform = mockMvc.perform(get(uri));

            // then
            perform
                .andDo(print())
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("http://localhost/login-page"));
        }
    }

    @Nested
    @DisplayName("[user-and-admin] 사용자와 관리자가 접근 가능한 API")
    class Describe_user_and_admin {

        @Test
        @DisplayName("[success] ADMIN 권한을 가진 사용자가 인증 받았을 때 API 호출에 성공하는지 확인한다.")
        @WithMockUser(username = "user", roles = "ADMIN")
        public void success() throws Exception {
            // given
            String uri = "/user-and-admin";

            // when
            ResultActions perform = mockMvc.perform(get(uri));

            // then
            perform
                .andDo(print())
                .andExpect(status().isOk());
        }

        @Test
        @DisplayName("[success] USER 권한을 가진 사용자가 인증 받았을 때 API 호출에 성공하는지 확인한다.")
        @WithMockUser(username = "user", roles = "USER")
        public void success2() throws Exception {
            // given
            String uri = "/user-and-admin";

            // when
            ResultActions perform = mockMvc.perform(get(uri));

            // then
            perform
                .andDo(print())
                .andExpect(status().isOk());
        }

        @Test
        @DisplayName("[success] USER 혹은 ADMIN 이 아닌 권한을 가진 사용자가 인증 받았을 때 에러 페이지로 리다이렉트되는지 확인한다.")
        @WithMockUser(username = "user", roles = "UNKOWN")
        public void success3() throws Exception {
            // given
            String uri = "/admin";

            // when
            ResultActions perform = mockMvc.perform(get(uri));

            // then
            perform
                .andDo(print())
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/error/access-denied"));
        }

        @Test
        @DisplayName("[success] 인증받지 않은 사용자가 API 를 호출할 때 로그인 페이지로 리다이렉트 되는지 확인한다. ")
        public void success4() throws Exception {
            // given
            String uri = "/user-and-admin";

            // when
            ResultActions perform = mockMvc.perform(get(uri));

            // then
            perform
                .andDo(print())
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("http://localhost/login-page"));
        }
    }

    @Nested
    @DisplayName("[all] 인증받은 사용자라면 누구나 접근 가능한 API")
    class Describe_all {

        @Test
        @DisplayName("[success] ADMIN 권한을 가진 사용자가 인증 받았을 때 API 호출에 성공하는지 확인한다.")
        @WithMockUser(username = "user", roles = "ADMIN")
        public void success() throws Exception {
            // given
            String uri = "/all";

            // when
            ResultActions perform = mockMvc.perform(get(uri));

            // then
            perform
                .andDo(print())
                .andExpect(status().isOk());
        }

        @Test
        @DisplayName("[success] USER 권한을 가진 사용자가 인증 받았을 때 API 호출에 성공하는지 확인한다.")
        @WithMockUser(username = "user", roles = "USER")
        public void success2() throws Exception {
            // given
            String uri = "/all";

            // when
            ResultActions perform = mockMvc.perform(get(uri));

            // then
            perform
                .andDo(print())
                .andExpect(status().isOk());
        }

        @Test
        @DisplayName("[success] UNKOWN 권한을 가진 사용자가 인증 받았을 때 API 호출에 성공하는지 확인한다.")
        @WithMockUser(username = "user", roles = "UNKOWN")
        public void success3() throws Exception {
            // given
            String uri = "/all";

            // when
            ResultActions perform = mockMvc.perform(get(uri));

            // then
            perform
                .andDo(print())
                .andExpect(status().isOk());
        }

        @Test
        @DisplayName("[success] 인증받지 않은 사용자가 API 를 호출할 때 로그인 페이지로 리다이렉트 되는지 확인한다. ")
        public void success4() throws Exception {
            // given
            String uri = "/user-and-admin";

            // when
            ResultActions perform = mockMvc.perform(get(uri));

            // then
            perform
                .andDo(print())
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("http://localhost/login-page"));
        }
    }
}
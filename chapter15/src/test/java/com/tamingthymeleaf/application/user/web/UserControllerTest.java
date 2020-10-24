package com.tamingthymeleaf.application.user.web;

import com.tamingthymeleaf.application.infrastructure.security.StubUserDetailsService;
import com.tamingthymeleaf.application.user.UserService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Bean;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.test.context.support.WithUserDetails;
import org.springframework.test.web.servlet.MockMvc;
import org.thymeleaf.spring5.templateresolver.SpringResourceTemplateResolver;
import org.thymeleaf.templateresolver.ITemplateResolver;

import static com.tamingthymeleaf.application.infrastructure.security.StubUserDetailsService.USERNAME_USER;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.redirectedUrl;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(UserController.class)
class UserControllerTest {

    @Autowired
    private MockMvc mockMvc;
    @MockBean
    private UserService userService;

    @Test
    void testGetUsersRedirectsToLoginWhenNotAuthenticated() throws Exception {
        mockMvc.perform(get("/users"))
               .andDo(print())
               .andExpect(status().is3xxRedirection())
               .andExpect(redirectedUrl("http://localhost/login"));
    }

    @Test
    @WithUserDetails(USERNAME_USER)
    void testGetUsersAsUser() throws Exception {

        when(userService.getUsers(any(Pageable.class)))
                .thenReturn(Page.empty());

        mockMvc.perform(get("/users"))
               .andDo(print())
               .andExpect(status().isOk());
    }

    @TestConfiguration
    static class TestConfig {
        @Bean
        public PasswordEncoder passwordEncoder() {
            return PasswordEncoderFactories.createDelegatingPasswordEncoder();
        }

        @Bean
        public ITemplateResolver svgTemplateResolver() {
            SpringResourceTemplateResolver resolver = new SpringResourceTemplateResolver();
            resolver.setPrefix("classpath:/templates/svg/");
            resolver.setSuffix(".svg");
            resolver.setTemplateMode("XML");

            return resolver;
        }

        @Bean
        public UserDetailsService userDetailsService(PasswordEncoder passwordEncoder) {
            return new StubUserDetailsService(passwordEncoder);
        }
    }
}

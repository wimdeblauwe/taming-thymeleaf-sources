package com.tamingthymeleaf.application.infrastructure.security;

import org.springframework.boot.security.autoconfigure.web.servlet.PathRequest;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableMethodSecurity(securedEnabled = true)
public class WebSecurityConfiguration {

    // tag::configure-http[]
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http.authorizeHttpRequests(authz -> authz
                    .requestMatchers(PathRequest.toStaticResources().atCommonLocations()).permitAll()
                    .requestMatchers("/svg/*", "/assets/**").permitAll()
                    .anyRequest().authenticated())
            .formLogin(formLogin -> formLogin.loginPage("/login")
                                             .permitAll())
            .logout(logout -> logout.permitAll());

        return http.build();
    }
    // end::configure-http[]
}

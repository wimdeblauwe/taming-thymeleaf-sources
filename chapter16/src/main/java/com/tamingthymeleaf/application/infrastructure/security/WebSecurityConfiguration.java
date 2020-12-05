package com.tamingthymeleaf.application.infrastructure.security;

import org.springframework.boot.autoconfigure.security.servlet.PathRequest;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;

// tag::configure-users[]
@Configuration
@EnableGlobalMethodSecurity(securedEnabled = true)
public class WebSecurityConfiguration extends WebSecurityConfigurerAdapter {
    private final PasswordEncoder passwordEncoder;
    private final UserDetailsService userDetailsService;

    public WebSecurityConfiguration(PasswordEncoder passwordEncoder,
                                    UserDetailsService userDetailsService) { //<.>
        this.passwordEncoder = passwordEncoder;
        this.userDetailsService = userDetailsService;
    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(userDetailsService) //<.>
            .passwordEncoder(passwordEncoder); //<.>
    }
    // end::configure-users[]

    // tag::configure-http[]
    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.csrf(httpSecurityCsrfConfigurer ->
                          httpSecurityCsrfConfigurer.ignoringAntMatchers("/api/integration-test/**")); //<.>
        http.authorizeRequests()
            .requestMatchers(PathRequest.toStaticResources().atCommonLocations()).permitAll()
            .antMatchers("/api/integration-test/**").permitAll() //<.>
            .antMatchers("/img/*").permitAll()
            .anyRequest().authenticated()
            .and()
            .formLogin()
            .loginPage("/login")
            .permitAll()
            .and()
            .logout().permitAll();
    }
    // end::configure-http[]
}

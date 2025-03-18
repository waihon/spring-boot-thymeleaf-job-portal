package com.waihon.springboot.thymeleaf.jobportal.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
public class WebSecurityConfig {

    private final String[] publicUrls = {
            "/",
            "/global-search/**",
            "/register",
            "/register/**",
            "/webjars/**",
            "/resources/**",
            "/summernote/**",
            "/assets/**",
            "/css/**",
            "/*.css",
            "/js/**",
            "/*.js",
            "/*.js.map",
            "/fonts/**",
            "/favicon.ico",
            "/error"
    };

    @Bean
    protected SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http.authorizeHttpRequests(auth -> {
            // Public URLs need not be authenticated.
            auth.requestMatchers(publicUrls).permitAll();
            // Any other requests will have to be authenticated.
            auth.anyRequest().authenticated();
        });

        return http.build();
    }

}

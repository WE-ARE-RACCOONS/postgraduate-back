package com.postgraduate.global.config.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.HeadersConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class SecurityConfig {
    private static final String[] PASS = {"/resource/**", "/css/**", "/js/**", "/img/**", "/lib/**"};
    @Bean
    public BCryptPasswordEncoder encodePassword() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    protected SecurityFilterChain config(HttpSecurity http) throws Exception {
        http
                .csrf(csrf->csrf.disable())
                .cors(cors->cors.disable())
                .httpBasic(httpBasic->httpBasic.disable())
                .headers((headers) -> headers.frameOptions(HeadersConfigurer.FrameOptionsConfig::disable));
        http
                .formLogin(formLogin->formLogin.disable())
                .logout(logout->logout.disable());
        http
                .authorizeHttpRequests(auth->auth
                        .requestMatchers(PASS).permitAll()
                        .anyRequest().permitAll())
                .sessionManagement(sessionManagement->sessionManagement.sessionCreationPolicy(SessionCreationPolicy.STATELESS));

        return http.build();
    }
}

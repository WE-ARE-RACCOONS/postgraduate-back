package com.postgraduate.global.config.security;

import com.postgraduate.global.jwt.JwtFilter;
import com.postgraduate.global.jwt.JwtProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.HeadersConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {
    private final JwtProvider jwtProvider;
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
                .authorizeHttpRequests((authorize) -> authorize
                        .requestMatchers(PASS).permitAll()
                        .requestMatchers("/swagger-ui/**", "/swagger-resources/**", "/v3/api-docs/**").permitAll()
                        .requestMatchers("/user/login/**").permitAll()
                        .requestMatchers("/user/refresh/**").permitAll()
                        .anyRequest().authenticated()
                )
                .sessionManagement((session) -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .exceptionHandling((exceptions) -> exceptions
                        .authenticationEntryPoint((request, response, authException) -> {
                            //TODO: 인증되지 않은 유저 예외
                            throw new IllegalAccessError("실패");
                        })
                        .accessDeniedHandler((request, response, authException) -> {
                            //TODO: 엑세스 거부 예외
                            throw new IllegalAccessError("실패");

                        })
                )
                .addFilterBefore(new JwtFilter(jwtProvider), UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }
}

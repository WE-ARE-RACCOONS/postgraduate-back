package com.postgraduate.global.config.security;

import com.postgraduate.domain.user.domain.entity.constant.Role;
import com.postgraduate.global.config.security.jwt.filter.CustomAccessDeniedHandler;
import com.postgraduate.global.config.security.jwt.filter.CustomAuthenticationEntryPoint;
import com.postgraduate.global.config.security.jwt.filter.JwtFilter;
import com.postgraduate.global.config.security.jwt.util.JwtUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.annotation.web.configurers.HeadersConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.encrypt.AesBytesEncryptor;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {
    private static final String[] PASS = {"/resource/**", "/css/**", "/js/**", "/img/**", "/lib/**"};
    private final JwtUtils jwtProvider;
    private final CustomAuthenticationEntryPoint customAuthenticationEntryPoint;
    private final CustomAccessDeniedHandler customAccessDeniedHandler;
    @Value("${aesBytesEncryptor.secret}")
    private String secretKey;
    @Value("${aesBytesEncryptor.salt}")
    private String salt;

    @Bean
    public AesBytesEncryptor aesBytesEncryptor() {
        return new AesBytesEncryptor(secretKey, salt);
    }

    @Bean
    protected SecurityFilterChain config(HttpSecurity http) throws Exception {
        http
                .csrf(csrf -> csrf.disable())
                .cors(corsConfigurer ->
                        corsConfigurer.configurationSource(source())
                )
                .httpBasic(AbstractHttpConfigurer::disable)
                .headers(headers -> headers.frameOptions(HeadersConfigurer.FrameOptionsConfig::disable));
        http
                .formLogin(AbstractHttpConfigurer::disable)
                .logout(logout -> logout.disable());
        http
                .authorizeHttpRequests(authorize -> authorize
                        .requestMatchers(PASS).permitAll()
                        .requestMatchers("/admin/**").hasAuthority(Role.ADMIN.name())
                        .requestMatchers("/adminServer/loginForm").permitAll()
                        .requestMatchers("/adminServer/login").permitAll()
                        .requestMatchers("/adminServer/**").hasAuthority(Role.ADMIN.name())
                        .requestMatchers("/bizppurio/**").hasAuthority(Role.ADMIN.name())
                        .requestMatchers(HttpMethod.PATCH, "/senior/**").hasAuthority(Role.SENIOR.name())
                        .requestMatchers(HttpMethod.POST, "/senior/**").hasAuthority(Role.SENIOR.name())
                        .requestMatchers("/senior/me/**").hasAuthority(Role.SENIOR.name())
                        .requestMatchers("/senior/search/**").permitAll()
                        .requestMatchers("/senior/field/**").permitAll()
                        .requestMatchers("/senior/all").permitAll()
                        .requestMatchers("/senior/{seniorId}").permitAll()
                        .requestMatchers("/senior/{seniorId}/times").hasAuthority(Role.USER.name())
                        .requestMatchers("/senior/{seniorId}/profile").hasAuthority(Role.USER.name())
                        .requestMatchers("/senior/{seniorId}/b").permitAll()
                        .requestMatchers("/senior/{seniorId}/times/b").hasAuthority(Role.USER.name())
                        .requestMatchers("/senior/{seniorId}/profile/b").hasAuthority(Role.USER.name())
                        .requestMatchers("/senior/**").hasAuthority(Role.SENIOR.name())

                        .requestMatchers("/user/nickname").permitAll()
                        .requestMatchers("/user/**").hasAuthority(Role.USER.name())

                        .requestMatchers("/image/upload/profile").authenticated()

                        .requestMatchers("/mentoring/applying").hasAuthority(Role.USER.name())
                        .requestMatchers("/mentoring/me/**").hasAuthority(Role.USER.name())
                        .requestMatchers("/mentoring/senior/**").hasAuthority(Role.SENIOR.name())

                        .requestMatchers("/salary/**").hasAuthority(Role.SENIOR.name())

                        .requestMatchers("/auth/user/token").hasAuthority(Role.SENIOR.name())
                        .requestMatchers("/auth/user/change").hasAuthority(Role.SENIOR.name())
                        .requestMatchers("/auth/senior/token").hasAuthority(Role.USER.name())
                        .requestMatchers("/auth/senior/change").hasAuthority(Role.USER.name())
                        .requestMatchers("/auth/refresh").authenticated()
                        .requestMatchers("/auth/logout").authenticated()
                        .requestMatchers("/signout/**").authenticated()

                        .anyRequest().permitAll()
                )
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .exceptionHandling(exceptions -> exceptions
                        .authenticationEntryPoint(customAuthenticationEntryPoint)
                        .accessDeniedHandler(customAccessDeniedHandler)
                )
                .addFilterBefore(new JwtFilter(jwtProvider), UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }

    @Bean
    public CorsConfigurationSource source() {
        CorsConfiguration configuration = new CorsConfiguration();
        configuration.addExposedHeader("Authorization");
        configuration.addAllowedOriginPattern("*");
        configuration.addAllowedHeader("*");
        configuration.addAllowedMethod("*");
        configuration.setAllowCredentials(true);
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }
}

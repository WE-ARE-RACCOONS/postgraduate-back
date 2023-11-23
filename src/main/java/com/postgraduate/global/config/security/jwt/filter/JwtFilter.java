package com.postgraduate.global.config.security.jwt.filter;

import com.postgraduate.global.config.security.jwt.JwtProvider;
import com.postgraduate.global.exception.ApplicationException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@RequiredArgsConstructor
@Slf4j
public class JwtFilter extends OncePerRequestFilter {
    private final JwtProvider jwtProvider;
    private final String AUTHORIZATION = "Authorization";
    private final String BEARER = "Bearer";

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        String token = resolveToken(request);
        if (token != null) {
            log.info("토큰 함께 요청 : {}", token);
            if (!jwtProvider.validateToken(response, token))
                return;
            try {
                Authentication authentication = jwtProvider.getAuthentication(response, token);
                SecurityContextHolder.getContext().setAuthentication(authentication);
                log.info("context 인증 정보 저장 : {}", authentication.getName());
            } catch (ApplicationException ex) {
                return;
            }
        }

        filterChain.doFilter(request, response);
    }

    private String resolveToken(HttpServletRequest request) {
        String bearerToken = request.getHeader(AUTHORIZATION);
        if (StringUtils.hasText(bearerToken) && bearerToken.startsWith(BEARER)) {
            return bearerToken.split(" ")[1];
        }
        return null;
    }
}


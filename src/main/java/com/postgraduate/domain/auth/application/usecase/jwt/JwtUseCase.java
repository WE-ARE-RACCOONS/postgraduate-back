package com.postgraduate.domain.auth.application.usecase.jwt;

import com.postgraduate.domain.auth.application.dto.JwtTokenResponse;
import com.postgraduate.domain.user.domain.entity.User;
import com.postgraduate.domain.user.domain.entity.constant.Role;
import com.postgraduate.global.jwt.JwtProvider;
import io.jsonwebtoken.Claims;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class JwtUseCase {
    private final JwtProvider jwtProvider;

    public JwtTokenResponse signIn(User user) {
        String accessToken = jwtProvider.generateToken(user.getUserId(), user.getRole(), false);
        String refreshToken = jwtProvider.generateToken(user.getUserId(), user.getRole(), true);
        return new JwtTokenResponse(accessToken, refreshToken);
    }

    public JwtTokenResponse regenerateToken(String refreshToken) {
        jwtProvider.validateToken(refreshToken);
        Claims claims = jwtProvider.parseClaims(refreshToken);
        String role = claims.get("role", String.class);
        String id = claims.getSubject();
        String newAccessToken = jwtProvider.generateToken(Long.valueOf(id), Role.valueOf(role), false);
        return new JwtTokenResponse(newAccessToken, null);
    }
}

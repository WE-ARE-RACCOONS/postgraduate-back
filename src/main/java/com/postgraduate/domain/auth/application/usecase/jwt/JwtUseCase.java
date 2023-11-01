package com.postgraduate.domain.auth.application.usecase.jwt;

import com.postgraduate.domain.auth.application.dto.res.JwtTokenResponse;
import com.postgraduate.domain.user.domain.entity.User;
import com.postgraduate.global.auth.AuthDetails;
import com.postgraduate.global.config.security.util.SecurityUtils;
import com.postgraduate.global.jwt.JwtProvider;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class JwtUseCase {
    private final JwtProvider jwtProvider;
    private final SecurityUtils securityUtils;
    @Value("${jwt.refreshExpiration}")
    private int refreshExpiration;
    @Value("${jwt.accessExpiration}")
    private int accessExpiration;

    public JwtTokenResponse signIn(User user) {
        return generateToken(user);
    }

    public JwtTokenResponse regenerateToken(AuthDetails authDetails, HttpServletRequest request) {
        User user = securityUtils.getLoggedInUser(authDetails);
        jwtProvider.checkRedis(user.getUserId(), request);
        return generateToken(user);
    }

    private JwtTokenResponse generateToken(User user) {
        String accessToken = jwtProvider.generateAccessToken(user.getUserId(), user.getRole());
        String refreshToken = jwtProvider.generateRefreshToken(user.getUserId(), user.getRole());
        return new JwtTokenResponse(accessToken, accessExpiration, refreshToken, refreshExpiration, user.getRole());
    }
}

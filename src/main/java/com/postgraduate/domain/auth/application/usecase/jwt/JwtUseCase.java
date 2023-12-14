package com.postgraduate.domain.auth.application.usecase.jwt;

import com.postgraduate.domain.auth.application.dto.res.JwtTokenResponse;
import com.postgraduate.domain.user.domain.entity.User;
import com.postgraduate.domain.user.exception.DeletedUserException;
import com.postgraduate.global.config.security.jwt.util.JwtUtils;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class JwtUseCase {
    private final JwtUtils jwtUtils;
    @Value("${jwt.refreshExpiration}")
    private int refreshExpiration;
    @Value("${jwt.accessExpiration}")
    private int accessExpiration;

    public JwtTokenResponse signIn(User user) {
        return generateToken(user);
    }
    
    public void logout(User user) {
        jwtUtils.makeExpired(user.getUserId());
    }

    public JwtTokenResponse regenerateToken(User user, HttpServletRequest request) {
        if (user.getIsDelete())
            throw new DeletedUserException();
        jwtUtils.checkRedis(user.getUserId(), request);
        return generateToken(user);
    }

    private JwtTokenResponse generateToken(User user) {
        if (user.getIsDelete())
            throw new DeletedUserException();
        String accessToken = jwtUtils.generateAccessToken(user.getUserId(), user.getRole());
        String refreshToken = jwtUtils.generateRefreshToken(user.getUserId(), user.getRole());
        return new JwtTokenResponse(accessToken, accessExpiration, refreshToken, refreshExpiration, user.getRole());
    }
}

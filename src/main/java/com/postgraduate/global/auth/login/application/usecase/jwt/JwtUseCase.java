package com.postgraduate.global.auth.login.application.usecase.jwt;

import com.postgraduate.domain.member.senior.exception.NoneSeniorException;
import com.postgraduate.domain.member.user.domain.entity.User;
import com.postgraduate.domain.member.user.domain.entity.constant.Role;
import com.postgraduate.domain.member.user.exception.DeletedUserException;
import com.postgraduate.domain.member.user.exception.UserNotFoundException;
import com.postgraduate.global.auth.login.application.dto.res.JwtTokenResponse;
import com.postgraduate.global.config.security.jwt.util.JwtUtils;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import static com.postgraduate.domain.member.user.domain.entity.constant.Role.*;

@RequiredArgsConstructor
@Service
@Slf4j
@Transactional(readOnly = true)
public class JwtUseCase {
    private final JwtUtils jwtUtils;
    @Value("${jwt.refreshExpiration}")
    private int refreshExpiration;
    @Value("${jwt.accessExpiration}")
    private int accessExpiration;

    public JwtTokenResponse signIn(User user) {
        if (user.isSenior())
            return seniorToken(user);
        if (user.isAdmin())
            return adminToken(user);
        return userToken(user);
    }

    public void logout(User user) {
        jwtUtils.makeExpired(user.getUserId());
    }

    public JwtTokenResponse regenerateToken(User user, HttpServletRequest request) {
        String role = jwtUtils.checkRedis(user.getUserId(), request);
        if (role.equals(SENIOR.toString()))
            return seniorToken(user);
        if (role.equals(ADMIN.toString()))
            return adminToken(user);
        return userToken(user);
    }

    public JwtTokenResponse changeUser(User user) {
        if (!user.isJunior())
            throw new UserNotFoundException();
        return userToken(user);
    }

    public JwtTokenResponse changeSenior(User user) {
        if (!user.isSenior())
            throw new NoneSeniorException();
        return seniorToken(user);
    }

    private JwtTokenResponse userToken(User user) {
        return generateToken(user, USER);
    }

    private JwtTokenResponse seniorToken(User user) {
        return generateToken(user, SENIOR);
    }

    private JwtTokenResponse adminToken(User user) {
        return generateToken(user, ADMIN);
    }

    private JwtTokenResponse generateToken(User user, Role role) {
        checkDelete(user);
        String accessToken = jwtUtils.generateAccessToken(user.getUserId(), role);
        String refreshToken = jwtUtils.generateRefreshToken(user.getUserId(), role);
        return new JwtTokenResponse(accessToken, accessExpiration, refreshToken, refreshExpiration, role, user.isTutorial());
    }

    private void checkDelete(User user) {
        if (user.isDelete())
            throw new DeletedUserException();
    }
}
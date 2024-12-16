package com.postgraduate.global.auth.login.application.usecase.jwt;

import com.postgraduate.domain.member.senior.exception.NoneSeniorException;
import com.postgraduate.domain.member.user.domain.entity.User;
import com.postgraduate.domain.member.user.domain.entity.constant.Role;
import com.postgraduate.domain.member.user.exception.DeletedUserException;
import com.postgraduate.domain.member.user.exception.UserNotFoundException;
import com.postgraduate.global.auth.login.application.dto.res.JwtTokenResponse;
import com.postgraduate.global.config.security.jwt.util.JwtUtils;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
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
    private static final String COOKIE = "role";
    @Value("${jwt.refreshExpiration}")
    private int refreshExpiration;
    @Value("${jwt.accessExpiration}")
    private int accessExpiration;

    public JwtTokenResponse signIn(User user, HttpServletResponse response) {
        if (user.isAdmin())
            return adminToken(user, response);
        if (user.isSenior())
            return seniorToken(user, response);
        return userToken(user, response);
    }

    public void logout(User user, HttpServletResponse response) {
        deleteRoleCookie(response);
        jwtUtils.makeExpired(user.getUserId());
    }

    public JwtTokenResponse regenerateToken(User user, HttpServletRequest request, HttpServletResponse response) {
        String role = jwtUtils.checkRedis(user.getUserId(), request);
        if (role.equals(SENIOR.toString()))
            return seniorToken(user, response);
        if (role.equals(ADMIN.toString()))
            return adminToken(user, response);
        return userToken(user, response);
    }

    public JwtTokenResponse changeUser(User user, HttpServletResponse response) {
        if (!user.isJunior())
            throw new UserNotFoundException();
        return userToken(user, response);
    }

    public JwtTokenResponse changeSenior(User user, HttpServletResponse response) {
        if (!user.isSenior())
            throw new NoneSeniorException();
        return seniorToken(user, response);
    }

    private JwtTokenResponse userToken(User user, HttpServletResponse response) {
        return generateToken(user, USER, response);
    }

    private JwtTokenResponse seniorToken(User user, HttpServletResponse response) {
        return generateToken(user, SENIOR, response);
    }

    private JwtTokenResponse adminToken(User user, HttpServletResponse response) {
        return generateToken(user, ADMIN, response);
    }

    private JwtTokenResponse generateToken(User user, Role role, HttpServletResponse response) {
        checkDelete(user);
        String accessToken = jwtUtils.generateAccessToken(user.getUserId(), role);
        String refreshToken = jwtUtils.generateRefreshToken(user.getUserId(), role);
        addRoleCookie(response, role);
        return new JwtTokenResponse(accessToken, accessExpiration, refreshToken, refreshExpiration, role, user.isTutorial());
    }

    private void checkDelete(User user) {
        if (user.isDelete())
            throw new DeletedUserException();
    }

    private void addRoleCookie(HttpServletResponse response, Role role) {
        Cookie roleCookie = new Cookie(COOKIE, role.toString());
        roleCookie.setHttpOnly(true);
        roleCookie.setPath("/");
        roleCookie.setMaxAge(accessExpiration);
        response.addCookie(roleCookie);
    }

    private void deleteRoleCookie(HttpServletResponse response) {
        Cookie roleCookie = new Cookie(COOKIE, null);
        roleCookie.setHttpOnly(true);
        roleCookie.setPath("/");
        roleCookie.setMaxAge(0);
        response.addCookie(roleCookie);
    }
}
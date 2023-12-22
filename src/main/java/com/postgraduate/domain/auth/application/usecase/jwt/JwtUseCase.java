package com.postgraduate.domain.auth.application.usecase.jwt;

import com.postgraduate.domain.auth.application.dto.res.JwtTokenResponse;
import com.postgraduate.domain.user.domain.entity.User;
import com.postgraduate.domain.user.domain.entity.constant.Role;
import com.postgraduate.domain.user.exception.DeletedUserException;
import com.postgraduate.domain.user.exception.UserNotFoundException;
import com.postgraduate.domain.wish.domain.service.WishGetService;
import com.postgraduate.global.config.security.jwt.util.JwtUtils;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import static com.postgraduate.domain.user.domain.entity.constant.Role.*;

@RequiredArgsConstructor
@Service
public class JwtUseCase {
    private final JwtUtils jwtUtils;
    private final WishGetService wishGetService;
    @Value("${jwt.refreshExpiration}")
    private int refreshExpiration;
    @Value("${jwt.accessExpiration}")
    private int accessExpiration;

    public JwtTokenResponse signIn(User user) {
        checkDelete(user);
        if (user.getRole() == SENIOR)
            return seniorToken(user);
        if (user.getRole() == ADMIN)
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
        checkDelete(user);
        return userToken(user);
    }

    public JwtTokenResponse changeSenior(User user) {
        checkDelete(user);
        return seniorToken(user);
    }

    private JwtTokenResponse userToken(User user) {
        if (wishGetService.byUser(user).isEmpty())
            throw new UserNotFoundException();
        return generateToken(user, USER);
    }

    private JwtTokenResponse seniorToken(User user) {
        checkDelete(user);
        return generateToken(user, SENIOR);
    }

    private JwtTokenResponse adminToken(User user) {
        return generateToken(user, ADMIN);
    }

    private JwtTokenResponse generateToken(User user, Role role) {
        String accessToken = jwtUtils.generateAccessToken(user.getUserId(), role);
        String refreshToken = jwtUtils.generateRefreshToken(user.getUserId(), role);
        return new JwtTokenResponse(accessToken, accessExpiration, refreshToken, refreshExpiration, role);
    }

    private void checkDelete(User user) {
        if (user.getIsDelete())
            throw new DeletedUserException();
    }
}

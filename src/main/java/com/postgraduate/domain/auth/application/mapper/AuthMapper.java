package com.postgraduate.domain.auth.application.mapper;

import com.postgraduate.domain.auth.application.dto.res.AuthUserResponse;
import com.postgraduate.domain.user.domain.entity.User;

import java.util.Optional;

public class AuthMapper {
    public static AuthUserResponse mapToAuthUser(User user, Long socialId) {
        return new AuthUserResponse(user, socialId);
    }

    public static AuthUserResponse mapToAuthUser(Long socialId) {
        return new AuthUserResponse(socialId);
    }
}

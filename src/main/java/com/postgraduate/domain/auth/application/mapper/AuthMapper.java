package com.postgraduate.domain.auth.application.mapper;

import com.postgraduate.domain.auth.application.dto.res.AuthUserResponse;
import com.postgraduate.domain.user.domain.entity.User;

import java.util.Optional;

public class AuthMapper {
    public static AuthUserResponse mapToAuthUser(Optional<User> user, Long socialId) {
        return AuthUserResponse.builder()
                .user(user)
                .socialId(socialId)
                .build();
    }
}

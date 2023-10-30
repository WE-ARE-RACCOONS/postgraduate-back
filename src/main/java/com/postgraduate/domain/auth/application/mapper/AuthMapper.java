package com.postgraduate.domain.auth.application.mapper;

import com.postgraduate.domain.auth.application.dto.AuthUserResponse;
import com.postgraduate.domain.user.domain.entity.User;

public class AuthMapper {
    public static AuthUserResponse mapToAuthUser(User user, Long socialId) {
        return AuthUserResponse.builder()
                .user(user)
                .socialId(socialId)
                .build();
    }
}

package com.postgraduate.domain.auth.application.mapper;

import com.postgraduate.domain.auth.application.dto.AuthUserResponse;
import com.postgraduate.domain.user.domain.entity.User;

public class AuthMapper {
    public static AuthUserResponse mapToAuthUser(User user, boolean isNew) {
        return AuthUserResponse.builder()
                .user(user)
                .isNew(isNew).build();
    }
}

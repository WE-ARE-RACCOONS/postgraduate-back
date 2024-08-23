package com.postgraduate.domain.auth.application.mapper;

import com.postgraduate.domain.auth.application.dto.res.AuthUserResponse;
import com.postgraduate.domain.user.user.domain.entity.User;

public class AuthMapper {
    private AuthMapper() {
        throw new IllegalArgumentException();
    }
    public static AuthUserResponse mapToAuthUser(User user, Long socialId) {
        return new AuthUserResponse(user, socialId);
    }

    public static AuthUserResponse mapToAuthUser(Long socialId, boolean isDelete) {
        return new AuthUserResponse(socialId, isDelete);
    }

    public static AuthUserResponse mapToAuthUser(Long socialId) {
        return new AuthUserResponse(socialId);
    }
}

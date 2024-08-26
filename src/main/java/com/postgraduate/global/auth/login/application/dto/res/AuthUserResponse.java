package com.postgraduate.global.auth.login.application.dto.res;

import com.postgraduate.domain.user.user.domain.entity.User;

public record AuthUserResponse(User user, Long socialId, Boolean isDelete) implements AuthResponse{
    public AuthUserResponse(Long socialId) {
        this(null, socialId, false);
    }

    public AuthUserResponse(User user, Long socialId) {
        this(user, socialId, false);
    }

    public AuthUserResponse(Long socialId, boolean isDelete) {
        this(null, socialId, isDelete);
    }
}
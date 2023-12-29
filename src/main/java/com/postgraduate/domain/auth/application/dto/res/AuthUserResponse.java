package com.postgraduate.domain.auth.application.dto.res;

import com.postgraduate.domain.user.domain.entity.User;

public record AuthUserResponse(User user, Long socialId) {
    public AuthUserResponse(Long socialId) {
        this(null, socialId);
    }
}
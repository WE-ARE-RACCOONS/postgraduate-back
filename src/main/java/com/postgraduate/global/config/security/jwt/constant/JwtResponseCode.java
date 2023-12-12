package com.postgraduate.global.config.security.jwt.constant;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum JwtResponseCode {
    EXPIRED_JWT("EX200"),
    INVALID_JWT("EX201"),
    NOT_FOUND_REFRESH_TOKEN("EX202"),
    INVALID_REFRESH_TOKEN("EX202"),
    DELETE_USER("EX203");

    private final String code;
}

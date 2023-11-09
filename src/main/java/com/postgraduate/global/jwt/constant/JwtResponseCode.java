package com.postgraduate.global.jwt.constant;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum JwtResponseCode {
    EXPIRED_JWT("EX200"),
    INVALID_JWT("EX201"),
    NONE_REFRESH_TOKEN("EX202"),
    INVALID_REFRESH_TOKEN("EX202");

    private final String code;
}

package com.postgraduate.global.config.security.jwt.constant;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum JwtResponseMessage {
    EXPIRED_JWT("만료된 토큰입니다."),
    INVALID_JWT("잘못된 토큰입니다."),
    INVALID_REFRESH_JWT("해당 리프레시 토큰이 존재하지 않습니다."),
    NOT_FOUND_REFRESH("해당 토큰이 없습니다.");

    private final String message;
}

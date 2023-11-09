package com.postgraduate.domain.auth.presentation.contant;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum AuthResponseCode {
    AUTH_FIND("AU200"),
    AUTH_UPDATE("AU201"),
    AUTH_CREATE("AU202"),
    AUTH_DELETE("AU203"),
    AUTH_ALREADY("AU204"),

    AUTH_NONE("EX900"),
    AUTH_INVALID_KAKAO("EX901");
    private final String code;
}

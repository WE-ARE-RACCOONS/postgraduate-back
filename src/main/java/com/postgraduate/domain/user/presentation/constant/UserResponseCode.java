package com.postgraduate.domain.user.presentation.constant;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
public enum UserResponseCode {
    USER_FIND("UR200"),
    USER_UPDATE("UR201"),
    USER_CREATE("UR202"),
    USER_DELETE("UR203"),

    NONE_USER("EX300");
    private final String code;
}

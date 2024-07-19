package com.postgraduate.domain.user.user.presentation.constant;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
public enum UserResponseCode {
    USER_FIND("UR200"),
    USER_UPDATE("UR201"),
    USER_CREATE("UR202"),
    USER_DELETE("UR203"),

    USER_NOT_FOUND("EX300"),
    USER_DELETED("EX301"),
    INVALID_PHONE_NUMBER("EX302");
    private final String code;
}

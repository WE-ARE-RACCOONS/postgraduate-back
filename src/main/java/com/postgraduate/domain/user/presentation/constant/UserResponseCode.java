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

    USER_NOT_FOUND("EX300"),
    USER_DELETED("EX301"),
    INVALID_PHONE_NUMBER("EX302"),

    WISH_FIND("WSH200"),
    WISH_UPDATE("WSH201"),
    WISH_CREATE("WSH202"),
    WISH_DELETE("WSH203"),

    WISH_NOT_FOUND("EX200"),
    MATCHING_NOT_AGREE("EX201"),
    WISH_EMPTY("EX202");

    private final String code;
}

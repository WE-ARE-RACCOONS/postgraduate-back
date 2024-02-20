package com.postgraduate.domain.wish.presentation.constant;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
public enum WishResponseCode {
    WISH_FIND("WSH200"),
    WISH_UPDATE("WSH201"),
    WISH_CREATE("WSH202"),
    WISH_DELETE("WSH203"),

    WISH_NOT_FOUND("EX200"),
    MATCHING_NOT_AGREE("EX201"),
    WISH_EMPTY("EX202");
    private final String code;
}

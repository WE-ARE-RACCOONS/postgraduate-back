package com.postgraduate.domain.wish.presentation.constant;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
public enum WishResponseCode {
    WISH_FIND("WS200"),
    WISH_UPDATE("WS201"),
    WISH_CREATE("WS202"),
    WISH_DELETE("WS203");

    private final String code;
}

package com.postgraduate.global.constant;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
public enum ErrorCode {
    VALID_BLANK("EX1100");
    private final String code;
}

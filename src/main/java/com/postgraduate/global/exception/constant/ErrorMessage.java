package com.postgraduate.global.exception.constant;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
public enum ErrorMessage {
    VALID_BLANK("빈값이 들어왔습니다.");
    private final String message;
}

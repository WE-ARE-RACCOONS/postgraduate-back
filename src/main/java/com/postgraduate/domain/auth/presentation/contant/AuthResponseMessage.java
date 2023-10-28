package com.postgraduate.domain.auth.presentation.contant;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum AuthResponseMessage {
    SUCCESS_AUTH_MESSAGE("사용자 인증에 성공하였습니다."),
    SUCCESS_REGENERATE_TOKEN_MESSAGE("토큰 재발급에 성공하였습니다.");
    private final String message;
}

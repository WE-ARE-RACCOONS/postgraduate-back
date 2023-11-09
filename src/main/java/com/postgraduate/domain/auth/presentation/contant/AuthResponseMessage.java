package com.postgraduate.domain.auth.presentation.contant;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum AuthResponseMessage {
    SUCCESS_AUTH_MESSAGE("사용자 인증에 성공하였습니다."),
    NOT_REGISTERED_USER_MESSAGE("가입하지 않은 유저입니다."),
    SUCCESS_REGENERATE_TOKEN_MESSAGE("토큰 재발급에 성공하였습니다."),

    PERMISSION_DENIED_MESSAGE("권한이 없습니다."),
    KAKAO_INVALID_MESSAGE("카카오 정보가 유효하지 않습니다.");
    private final String message;
}

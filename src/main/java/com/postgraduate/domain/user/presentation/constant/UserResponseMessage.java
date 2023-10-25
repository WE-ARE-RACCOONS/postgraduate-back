package com.postgraduate.domain.user.presentation.constant;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum UserResponseMessage {
    GET_USER_INFO("사용자 기본 정보 조회에 성공하였습니다.");

    private final String message;
}

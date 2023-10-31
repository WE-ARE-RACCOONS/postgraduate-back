package com.postgraduate.domain.senior.presentation.constant;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum SeniorResponseMessage {
    SUCCESS_SENIOR_SIGN_UP_MESSAGE("대학원생 가입에 성공하였습니다.");

    private final String message;
}

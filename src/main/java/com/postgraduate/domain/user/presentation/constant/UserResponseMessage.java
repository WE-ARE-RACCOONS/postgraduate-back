package com.postgraduate.domain.user.presentation.constant;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum UserResponseMessage {
    GET_USER_INFO("사용자 기본 정보 조회에 성공하였습니다."),
    GET_NICKNAME_CHECK("닉네임 중복체크에 성공하였습니다."),
    UPDATE_USER_INFO("사용자 업데이트에 성공하였습니다.");

    private final String message;
}

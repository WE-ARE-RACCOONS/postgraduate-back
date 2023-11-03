package com.postgraduate.domain.senior.presentation.constant;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum SeniorResponseMessage {
    CREATE_SENIOR("대학원생 가입에 성공하였습니다."),
    UPDATE_PROFILE("대학원생 프로필 등록에 성공하였습니다"),
    GET_SENIOR_INFO("대학원생 정보 조회에 성공하였습니다"),
    UPDATE_CERTIFICATION("대학원생 인증사진 업로드에 성공하였습니다");

    private final String message;
}

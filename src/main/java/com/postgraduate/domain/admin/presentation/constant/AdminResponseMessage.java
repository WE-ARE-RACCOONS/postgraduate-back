package com.postgraduate.domain.admin.presentation.constant;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum AdminResponseMessage {
    GET_DETAILS("상세 조회에 성공하였습니다."),
    GET_LIST("목록 조회에 성공하였습니다."),
    UPDATE_SENIOR_STATUS("선배 승인 상태 수정에 성공하였습니다."),
    UPDATE_SALARY_STATUS("정산 상태 수정에 성공하였습니다."),
    UPDATE_WISH_STATUS("매칭 지원 완료에 성공하였습니다.");

    private final String message;
}

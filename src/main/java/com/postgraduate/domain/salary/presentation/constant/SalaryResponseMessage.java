package com.postgraduate.domain.salary.presentation.constant;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum SalaryResponseMessage {
    GET_SALARY_INFO("정산 정보 조회에 성공하였습니다"),
    GET_SALARY_LIST_INFO("정산 리스트 조회에 성공하였습니다."),

    NOT_FOUND_SALARY("정산을 찾을 수 없습니다.");

    private final String message;
}

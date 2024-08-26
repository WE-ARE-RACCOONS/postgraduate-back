package com.postgraduate.domain.senior.account.presentation.constant;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum AccountResponseMessage {
    GET_ACCOUNT_INFO("계좌 정보 조회에 성공하였습니다"),
    GET_ACCOUNT_LIST_INFO("계좌 리스트 조회에 성공하였습니다."),

    NOT_FOUND_ACCOUNT("계좌를 찾을 수 없습니다.");

    private final String message;
}

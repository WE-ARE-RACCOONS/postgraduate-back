package com.postgraduate.domain.payment.presentation.constant;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum PaymentResponseMessage {
    GET_PAYMENT_LIST_INFO("결제 리스트 조회에 성공하였습니다."),
    GET_PAYMENT_DETAIL_INFO("결제 상세 조회에 성공하였습니다."),
    CREATE_PAYMENT("결제에 성공하였습니다."),
    UPDATE_PAYMENT("결제 정보 갱신에 성공하였습니다."),

    NOT_FOUND_PAYMENT("결제 내역이 존재하지 않습니다."),
    DENIED_CERTIFICATION("인증에 실패하였습니다."),
    FAIL_PAYMENT("결제가 실패하였습니다."),
    FAIL_REFUND("환불에 실패하였습니다."),
    ;

    private final String message;
}

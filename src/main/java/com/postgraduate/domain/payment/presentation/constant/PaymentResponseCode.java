package com.postgraduate.domain.payment.presentation.constant;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
public enum PaymentResponseCode {
    PAYMENT_FIND("PM200"),
    PAYMENT_UPDATE("PM201"),
    PAYMENT_CREATE("PM202"),
    PAYMENT_DELETE("PM203"),

    PAYMENT_NOT_FOUND("EX600"),
    PAYMENT_FAIL("EX601");
    private final String code;
}

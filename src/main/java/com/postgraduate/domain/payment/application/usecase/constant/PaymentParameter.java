package com.postgraduate.domain.payment.application.usecase.constant;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum PaymentParameter {
    SUCCESS("success"),
    REFERER("Referer"),
    CUSTOMER_ID("cst_id"),
    CUSTOMER_KEY("custKey"),
    FLAG("PCD_PAYCANCEL_FLAG"),
    CST_ID("PCD_CST_ID"),
    CUST_KEY("PCD_CUST_KEY"),
    AUTH_KEY("PCD_AUTH_KEY"),
    REF_KEY("PCD_REFUND_KEY"),
    PAYCANCEL_FLAG("PCD_PAYCANCEL_FLAG"),
    OID("PCD_PAY_OID"),
    DATE("PCD_PAY_DATE"),
    TOTAL("PCD_REFUND_TOTAL");

    private final String name;
}

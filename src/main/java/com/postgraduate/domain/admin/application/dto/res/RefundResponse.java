package com.postgraduate.domain.admin.application.dto.res;

public record RefundResponse(
        String PCD_PAY_RST,
        String PCD_PAY_CODE,
        String PCD_PAY_MSG,
        String PCD_PAY_OID,
        String PCD_PAY_GOODS,
        String PCD_REFUND_TOTAL,
        String PCD_PAY_TIME,
        String PCD_PAY_CARDTRADENUM,
        String PCD_PAY_CARDRECEIPT
) {
}

package com.postgraduate.domain.payment.exception;

import com.postgraduate.global.exception.ApplicationException;

import static com.postgraduate.domain.payment.presentation.constant.PaymentResponseMessage.FAIL_REFUND;
import static com.postgraduate.domain.payment.presentation.constant.PaymentResponseCode.REFUND_FAIL;


public class RefundFailException extends ApplicationException {

    public RefundFailException() {
        super(FAIL_REFUND.getMessage(), REFUND_FAIL.getCode());
    }
}

package com.postgraduate.domain.payment.exception;

import com.postgraduate.global.exception.ApplicationException;

import static com.postgraduate.domain.payment.presentation.constant.PaymentResponseMessage.FAIL_REFUND;


public class RefundFailException extends ApplicationException {

    public RefundFailException(String code) {
        super(FAIL_REFUND.getMessage(), code);
    }
}

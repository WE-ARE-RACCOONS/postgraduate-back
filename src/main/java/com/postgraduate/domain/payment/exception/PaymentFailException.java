package com.postgraduate.domain.payment.exception;

import com.postgraduate.global.exception.ApplicationException;

import static com.postgraduate.domain.payment.presentation.constant.PaymentResponseCode.PAYMENT_FAIL;
import static com.postgraduate.domain.payment.presentation.constant.PaymentResponseMessage.FAIL_PAYMENT;


public class PaymentFailException extends ApplicationException {

    public PaymentFailException() {
        super(FAIL_PAYMENT.getMessage(), PAYMENT_FAIL.getCode());
    }
}

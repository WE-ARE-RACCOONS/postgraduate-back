package com.postgraduate.domain.payment.exception;

import com.postgraduate.global.exception.ApplicationException;

import static com.postgraduate.domain.payment.presentation.constant.PaymentResponseCode.PAYMENT_NOT_FOUND;
import static com.postgraduate.domain.payment.presentation.constant.PaymentResponseMessage.NOT_FOUND_PAYMENT;


public class PaymentNotFoundException extends ApplicationException {

    public PaymentNotFoundException() {
        super(NOT_FOUND_PAYMENT.getMessage(), PAYMENT_NOT_FOUND.getCode());
    }
}

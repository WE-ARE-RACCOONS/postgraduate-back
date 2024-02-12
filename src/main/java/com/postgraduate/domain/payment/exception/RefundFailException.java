package com.postgraduate.domain.payment.exception;

import com.postgraduate.global.exception.ApplicationException;

import static com.postgraduate.domain.payment.presentation.constant.PaymentResponseMessage.DENIED_CERTIFICATION;


public class RefundFailException extends ApplicationException {

    public RefundFailException(String code) {
        super(DENIED_CERTIFICATION.getMessage(), code);
    }
}

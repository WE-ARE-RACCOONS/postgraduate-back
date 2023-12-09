package com.postgraduate.domain.payment.exception;

import com.postgraduate.global.exception.ApplicationException;

public class PaymentException extends ApplicationException {
    protected PaymentException(String message, String errorCode) {
        super(message, errorCode);
    }
}

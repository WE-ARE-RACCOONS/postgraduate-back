package com.postgraduate.domain.payment.exception;

import com.postgraduate.global.exception.ApplicationException;

import static com.postgraduate.domain.payment.presentation.constant.PaymentResponseMessage.DENIED_CERTIFICATION;


public class CertificationFailException extends ApplicationException {

    public CertificationFailException(String code) {
        super(DENIED_CERTIFICATION.getMessage(), code);
    }
}

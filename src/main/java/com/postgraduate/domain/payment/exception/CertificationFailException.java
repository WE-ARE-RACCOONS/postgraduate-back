package com.postgraduate.domain.payment.exception;

import com.postgraduate.global.exception.ApplicationException;

import static com.postgraduate.domain.payment.presentation.constant.PaymentResponseCode.CERTIFICATION_DENIED;
import static com.postgraduate.domain.payment.presentation.constant.PaymentResponseCode.PAYMENT_NOT_FOUND;
import static com.postgraduate.domain.payment.presentation.constant.PaymentResponseMessage.DENIED_CERTIFICATION;
import static com.postgraduate.domain.payment.presentation.constant.PaymentResponseMessage.NOT_FOUND_PAYMENT;


public class CertificationFailException extends ApplicationException {

    public CertificationFailException() {
        super(DENIED_CERTIFICATION.getMessage(), CERTIFICATION_DENIED.getCode());
    }
}

package com.postgraduate.domain.senior.exception;

import com.postgraduate.global.exception.ApplicationException;

public class SeniorCertificationException extends ApplicationException {
    public SeniorCertificationException() {
        super("이미승인", "code");
    }
}

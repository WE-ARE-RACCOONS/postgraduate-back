package com.postgraduate.domain.admin.exception;

import com.postgraduate.global.exception.ApplicationException;

public class SeniorException extends ApplicationException {
    protected SeniorException(String message, String errorCode) {
        super(message, errorCode);
    }
}

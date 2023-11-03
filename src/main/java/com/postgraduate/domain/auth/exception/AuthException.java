package com.postgraduate.domain.auth.exception;

import com.postgraduate.global.exception.ApplicationException;

public class AuthException extends ApplicationException {
    protected AuthException(String message, String errorCode) {
        super(message, errorCode);
    }
}

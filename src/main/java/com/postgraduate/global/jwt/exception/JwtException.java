package com.postgraduate.global.jwt.exception;

import com.postgraduate.global.exception.ApplicationException;

public class JwtException extends ApplicationException {
    protected JwtException(String message, String errorCode) {
        super(message, errorCode);
    }
}

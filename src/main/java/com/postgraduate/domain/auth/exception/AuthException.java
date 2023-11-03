package com.postgraduate.domain.auth.exception;

import com.postgraduate.global.exception.ApplicationException;
import org.springframework.http.HttpStatus;

public class AuthException extends ApplicationException {
    protected AuthException(String message, String errorCode, HttpStatus httpStatus) {
        super(message, errorCode, httpStatus);
    }
}

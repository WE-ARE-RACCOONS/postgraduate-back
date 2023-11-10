package com.postgraduate.domain.user.application.exception;

import com.postgraduate.global.exception.ApplicationException;

public class UserException extends ApplicationException {
    protected UserException(String message, String errorCode) {
        super(message, errorCode);
    }
}

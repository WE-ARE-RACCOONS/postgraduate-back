package com.postgraduate.domain.available.exception;

import com.postgraduate.global.exception.ApplicationException;

public class AvailableException extends ApplicationException {
    public AvailableException(String message, String errorCode) {
        super(message, errorCode);
    }
}

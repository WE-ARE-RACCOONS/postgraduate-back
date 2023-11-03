package com.postgraduate.global.exception;

import lombok.Getter;

@Getter
public abstract class ApplicationException extends RuntimeException{
    private final String errorCode;

    protected ApplicationException(String message, String errorCode) {
        super(message);
        this.errorCode = errorCode;
    }
}
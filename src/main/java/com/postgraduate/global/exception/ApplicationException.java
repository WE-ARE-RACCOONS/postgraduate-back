package com.postgraduate.global.exception;

import lombok.Getter;

@Getter
public abstract class ApplicationException extends RuntimeException{
    private final String code;

    protected ApplicationException(String message, String code) {
        super(message);
        this.code = code;
    }
}
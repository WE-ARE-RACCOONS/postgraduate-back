package com.postgraduate.domain.image.exception;

import com.postgraduate.global.exception.ApplicationException;

public class ImageException extends ApplicationException {
    protected ImageException(String message, String errorCode) {
        super(message, errorCode);
    }
}

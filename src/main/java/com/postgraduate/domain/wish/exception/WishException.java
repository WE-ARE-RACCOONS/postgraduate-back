package com.postgraduate.domain.wish.exception;

import com.postgraduate.global.exception.ApplicationException;

public class WishException extends ApplicationException {

    protected WishException(String message, String errorCode) {
        super(message, errorCode);
    }
}

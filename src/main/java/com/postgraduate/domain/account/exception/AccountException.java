package com.postgraduate.domain.account.exception;

import com.postgraduate.global.exception.ApplicationException;

public class AccountException extends ApplicationException {

    protected AccountException(String message, String errorCode) {
        super(message, errorCode);
    }
}

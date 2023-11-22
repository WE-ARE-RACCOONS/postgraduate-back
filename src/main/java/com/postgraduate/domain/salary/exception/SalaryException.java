package com.postgraduate.domain.salary.exception;

import com.postgraduate.global.exception.ApplicationException;

public class SalaryException extends ApplicationException {

    protected SalaryException(String message, String errorCode) {
        super(message, errorCode);
    }
}

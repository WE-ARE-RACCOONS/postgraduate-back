package com.postgraduate.domain.mentoring.exception;

import com.postgraduate.global.exception.ApplicationException;

public class MentoringException extends ApplicationException {
    protected MentoringException(String message, String errorCode) {
        super(message, errorCode);
    }
}

package com.postgraduate.domain.mentoring.exception;

import com.postgraduate.global.exception.ApplicationException;
import org.springframework.http.HttpStatus;

public class MentoringException extends ApplicationException {
    protected MentoringException(String message, String errorCode, HttpStatus httpStatus) {
        super(message, errorCode, httpStatus);
    }
}

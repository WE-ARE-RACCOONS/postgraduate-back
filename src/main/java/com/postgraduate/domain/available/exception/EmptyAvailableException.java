package com.postgraduate.domain.available.exception;

import com.postgraduate.global.exception.ApplicationException;

import static com.postgraduate.domain.senior.presentation.constant.SeniorResponseCode.TIME_EMPTY;
import static com.postgraduate.domain.senior.presentation.constant.SeniorResponseMessage.EMPTY_TIME;

public class EmptyAvailableException extends ApplicationException {
    public EmptyAvailableException() {
        super(EMPTY_TIME.getMessage(), TIME_EMPTY.getCode());
    }
}

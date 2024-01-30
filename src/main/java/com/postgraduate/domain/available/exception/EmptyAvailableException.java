package com.postgraduate.domain.available.exception;

import static com.postgraduate.domain.senior.presentation.constant.SeniorResponseCode.TIME_EMPTY;
import static com.postgraduate.domain.senior.presentation.constant.SeniorResponseMessage.EMPTY_TIME;

public class EmptyAvailableException extends AvailableException {
    public EmptyAvailableException() {
        super(EMPTY_TIME.getMessage(), TIME_EMPTY.getCode());
    }
}

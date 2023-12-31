package com.postgraduate.domain.available.exception;

import com.postgraduate.domain.senior.presentation.constant.SeniorResponseCode;
import com.postgraduate.domain.senior.presentation.constant.SeniorResponseMessage;

public class EmptyAvailableException extends AvailableException{
    public EmptyAvailableException() {
        super(SeniorResponseMessage.EMPTY_TIME.getMessage(), SeniorResponseCode.EMPTY_TIME.getCode());
    }
}

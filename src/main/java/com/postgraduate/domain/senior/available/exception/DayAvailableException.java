package com.postgraduate.domain.senior.available.exception;

import com.postgraduate.domain.senior.presentation.constant.SeniorResponseCode;
import com.postgraduate.domain.senior.presentation.constant.SeniorResponseMessage;
import com.postgraduate.global.exception.ApplicationException;

public class DayAvailableException extends ApplicationException {
    public DayAvailableException() {
        super(SeniorResponseMessage.INVALID_DAY.getMessage(), SeniorResponseCode.INVALID_DAY.getCode());
    }
}

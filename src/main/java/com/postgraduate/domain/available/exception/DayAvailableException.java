package com.postgraduate.domain.available.exception;

import com.postgraduate.domain.senior.presentation.constant.SeniorResponseCode;
import com.postgraduate.domain.senior.presentation.constant.SeniorResponseMessage;

public class DayAvailableException extends AvailableException {
    public DayAvailableException() {
        super(SeniorResponseMessage.INVALID_DAY.getMessage(), SeniorResponseCode.INVALID_DAY.getCode());
    }
}

package com.postgraduate.domain.member.senior.exception;

import com.postgraduate.domain.member.senior.presentation.constant.SeniorResponseCode;
import com.postgraduate.domain.member.senior.presentation.constant.SeniorResponseMessage;
import com.postgraduate.global.exception.ApplicationException;

public class DayAvailableException extends ApplicationException {
    public DayAvailableException() {
        super(SeniorResponseMessage.INVALID_DAY.getMessage(), SeniorResponseCode.INVALID_DAY.getCode());
    }
}

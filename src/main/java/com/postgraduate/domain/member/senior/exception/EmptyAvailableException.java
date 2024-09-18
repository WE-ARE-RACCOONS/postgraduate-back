package com.postgraduate.domain.member.senior.exception;

import com.postgraduate.domain.member.senior.presentation.constant.SeniorResponseCode;
import com.postgraduate.domain.member.senior.presentation.constant.SeniorResponseMessage;
import com.postgraduate.global.exception.ApplicationException;

public class EmptyAvailableException extends ApplicationException {
    public EmptyAvailableException() {
        super(SeniorResponseMessage.EMPTY_TIME.getMessage(), SeniorResponseCode.TIME_EMPTY.getCode());
    }
}

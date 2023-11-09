package com.postgraduate.domain.senior.exception;

import com.postgraduate.domain.senior.presentation.constant.SeniorResponseCode;
import com.postgraduate.domain.senior.presentation.constant.SeniorResponseMessage;

public class NoneSeniorException extends SeniorException{
    public NoneSeniorException() {
        super(SeniorResponseMessage.NONE_SENIOR.getMessage(), SeniorResponseCode.NONE_SENIOR.getCode());
    }
}

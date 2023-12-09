package com.postgraduate.domain.admin.exception;

import com.postgraduate.domain.senior.presentation.constant.SeniorResponseCode;
import com.postgraduate.domain.senior.presentation.constant.SeniorResponseMessage;

public class SeniorNotWaitingException extends SeniorException{
    public SeniorNotWaitingException() {
        super(SeniorResponseMessage.NOT_WAITING_STATUS.getMessage(), SeniorResponseCode.NOT_WAITING_STATUS.getCode());
    }
}

package com.postgraduate.domain.admin.exception;

import static com.postgraduate.domain.senior.presentation.constant.SeniorResponseCode.NONE_SENIOR;
import static com.postgraduate.domain.senior.presentation.constant.SeniorResponseMessage.NOT_WAITING_STATUS;

public class SeniorNotWaitingException extends SeniorException{
    public SeniorNotWaitingException() {
        super(NOT_WAITING_STATUS.getMessage(), NONE_SENIOR.getCode());
    }
}

package com.postgraduate.domain.admin.exception;

import com.postgraduate.global.exception.ApplicationException;

import static com.postgraduate.domain.senior.presentation.constant.SeniorResponseCode.STATUS_NOT_WAITING;
import static com.postgraduate.domain.senior.presentation.constant.SeniorResponseMessage.NOT_WAITING_STATUS;

public class SeniorNotWaitingException extends ApplicationException {
    public SeniorNotWaitingException() {
        super(NOT_WAITING_STATUS.getMessage(), STATUS_NOT_WAITING.getCode());
    }
}

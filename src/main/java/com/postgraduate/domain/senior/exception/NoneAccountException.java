package com.postgraduate.domain.senior.exception;

import com.postgraduate.domain.senior.presentation.constant.SeniorResponseCode;
import com.postgraduate.domain.senior.presentation.constant.SeniorResponseMessage;
import com.postgraduate.global.exception.ApplicationException;

public class NoneAccountException extends ApplicationException {
    public NoneAccountException() {
        super(SeniorResponseMessage.NONE_ACCOUNT.getMessage(), SeniorResponseCode.NONE_ACCOUNT.getCode());
    }
}

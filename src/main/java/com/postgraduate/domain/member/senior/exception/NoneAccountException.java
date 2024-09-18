package com.postgraduate.domain.member.senior.exception;

import com.postgraduate.domain.member.senior.presentation.constant.SeniorResponseCode;
import com.postgraduate.domain.member.senior.presentation.constant.SeniorResponseMessage;
import com.postgraduate.global.exception.ApplicationException;

public class NoneAccountException extends ApplicationException {
    public NoneAccountException() {
        super(SeniorResponseMessage.NOT_FOUND_ACCOUNT.getMessage(), SeniorResponseCode.ACCOUNT_NOT_FOUND.getCode());
    }
}

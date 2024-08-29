package com.postgraduate.domain.member.senior.exception;

import com.postgraduate.domain.member.senior.presentation.constant.SeniorResponseCode;
import com.postgraduate.domain.member.senior.presentation.constant.SeniorResponseMessage;
import com.postgraduate.global.exception.ApplicationException;

public class KeywordException extends ApplicationException {
    public KeywordException() {
        super(SeniorResponseMessage.INVALID_KEYWORD.getMessage(), SeniorResponseCode.INVALID_KEYWORD.getCode());
    }
}

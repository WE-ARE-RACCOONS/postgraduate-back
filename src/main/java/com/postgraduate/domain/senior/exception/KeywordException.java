package com.postgraduate.domain.senior.exception;

import com.postgraduate.domain.senior.presentation.constant.SeniorResponseCode;
import com.postgraduate.domain.senior.presentation.constant.SeniorResponseMessage;

public class KeywordException extends SeniorException {
    public KeywordException() {
        super(SeniorResponseMessage.INVALID_KEYWORD.getMessage(), SeniorResponseCode.INVALID_KEYWORD.getCode());
    }
}

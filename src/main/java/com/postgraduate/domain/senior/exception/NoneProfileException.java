package com.postgraduate.domain.senior.exception;

import com.postgraduate.domain.senior.presentation.constant.SeniorResponseCode;
import com.postgraduate.domain.senior.presentation.constant.SeniorResponseMessage;
import com.postgraduate.global.exception.ApplicationException;

public class NoneProfileException extends ApplicationException {
    public NoneProfileException() {
        super(SeniorResponseMessage.NOT_FOUND_PROFILE.getMessage(), SeniorResponseCode.PROFILE_NOT_FOUND.getCode());
    }
}

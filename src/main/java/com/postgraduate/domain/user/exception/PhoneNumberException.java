package com.postgraduate.domain.user.exception;

import com.postgraduate.domain.user.presentation.constant.UserResponseCode;
import com.postgraduate.domain.user.presentation.constant.UserResponseMessage;
import com.postgraduate.global.exception.ApplicationException;

public class PhoneNumberException extends ApplicationException {
    public PhoneNumberException() {
        super(UserResponseMessage.INVALID_PHONE_NUMBER.getMessage(), UserResponseCode.INVALID_PHONE_NUMBER.getCode());
    }
}

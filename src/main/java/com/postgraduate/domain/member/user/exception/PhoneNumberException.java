package com.postgraduate.domain.member.user.exception;

import com.postgraduate.domain.member.user.presentation.constant.UserResponseCode;
import com.postgraduate.domain.member.user.presentation.constant.UserResponseMessage;
import com.postgraduate.global.exception.ApplicationException;

public class PhoneNumberException extends ApplicationException {
    public PhoneNumberException() {
        super(UserResponseMessage.INVALID_PHONE_NUMBER.getMessage(), UserResponseCode.INVALID_PHONE_NUMBER.getCode());
    }
}

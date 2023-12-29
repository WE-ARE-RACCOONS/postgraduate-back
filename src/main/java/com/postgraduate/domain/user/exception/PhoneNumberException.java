package com.postgraduate.domain.user.exception;

import com.postgraduate.domain.user.presentation.constant.UserResponseCode;
import com.postgraduate.domain.user.presentation.constant.UserResponseMessage;

public class PhoneNumberException extends UserException{
    public PhoneNumberException() {
        super(UserResponseMessage.INVALID_PHONE_NUMBER.getMessage(), UserResponseCode.INVALID_PHONE_NUMBER.getCode());
    }
}

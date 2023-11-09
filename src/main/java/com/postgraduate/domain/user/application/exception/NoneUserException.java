package com.postgraduate.domain.user.application.exception;

import com.postgraduate.domain.user.presentation.constant.UserResponseCode;
import com.postgraduate.domain.user.presentation.constant.UserResponseMessage;

public class NoneUserException extends UserException{
    public NoneUserException() {
        super(UserResponseMessage.NONE_USER.getMessage(), UserResponseCode.NONE_USER.getCode());
    }
}

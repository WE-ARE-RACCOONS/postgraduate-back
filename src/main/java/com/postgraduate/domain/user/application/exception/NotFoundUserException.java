package com.postgraduate.domain.user.application.exception;

import com.postgraduate.domain.user.presentation.constant.UserResponseCode;
import com.postgraduate.domain.user.presentation.constant.UserResponseMessage;

public class NotFoundUserException extends UserException{
    public NotFoundUserException() {
        super(UserResponseMessage.NOT_FOUND_USER.getMessage(), UserResponseCode.NOT_FOUND_USER.getCode());
    }
}

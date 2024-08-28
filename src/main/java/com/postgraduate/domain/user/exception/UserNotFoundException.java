package com.postgraduate.domain.user.exception;

import com.postgraduate.domain.user.presentation.constant.UserResponseCode;
import com.postgraduate.domain.user.presentation.constant.UserResponseMessage;
import com.postgraduate.global.exception.ApplicationException;

public class UserNotFoundException extends ApplicationException {
    public UserNotFoundException() {
        super(UserResponseMessage.NOT_FOUND_USER.getMessage(), UserResponseCode.USER_NOT_FOUND.getCode());
    }
}

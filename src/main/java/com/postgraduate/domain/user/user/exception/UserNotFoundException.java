package com.postgraduate.domain.user.user.exception;

import com.postgraduate.global.exception.ApplicationException;

import static com.postgraduate.domain.user.user.presentation.constant.UserResponseCode.USER_NOT_FOUND;
import static com.postgraduate.domain.user.user.presentation.constant.UserResponseMessage.NOT_FOUND_USER;

public class UserNotFoundException extends ApplicationException {
    public UserNotFoundException() {
        super(NOT_FOUND_USER.getMessage(), USER_NOT_FOUND.getCode());
    }
}

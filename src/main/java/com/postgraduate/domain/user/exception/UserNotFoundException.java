package com.postgraduate.domain.user.exception;

import static com.postgraduate.domain.user.presentation.constant.UserResponseCode.USER_NOT_FOUND;
import static com.postgraduate.domain.user.presentation.constant.UserResponseMessage.NOT_FOUND_USER;

public class UserNotFoundException extends UserException {
    public UserNotFoundException() {
        super(NOT_FOUND_USER.getMessage(), USER_NOT_FOUND.getCode());
    }
}

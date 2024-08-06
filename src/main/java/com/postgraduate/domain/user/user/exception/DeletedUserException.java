package com.postgraduate.domain.user.user.exception;

import com.postgraduate.domain.user.user.presentation.constant.UserResponseCode;
import com.postgraduate.domain.user.user.presentation.constant.UserResponseMessage;
import com.postgraduate.global.exception.ApplicationException;

public class DeletedUserException extends ApplicationException {
    public DeletedUserException() {
        super(UserResponseMessage.DELETED_USER.getMessage(), UserResponseCode.USER_DELETED.getCode());
    }
}

package com.postgraduate.domain.user.exception;

import com.postgraduate.domain.user.presentation.constant.UserResponseCode;
import com.postgraduate.domain.user.presentation.constant.UserResponseMessage;

public class DeletedUserException extends UserException {
    public DeletedUserException() {
        super(UserResponseMessage.DELETED_USER.getMessage(), UserResponseCode.USER_DELETED.getCode());
    }
}

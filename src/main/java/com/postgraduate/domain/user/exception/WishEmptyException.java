package com.postgraduate.domain.user.exception;

import com.postgraduate.domain.user.presentation.constant.UserResponseCode;
import com.postgraduate.domain.user.presentation.constant.UserResponseMessage;
import com.postgraduate.global.exception.ApplicationException;

public class WishEmptyException extends ApplicationException {
    public WishEmptyException() {
        super(UserResponseMessage.EMPTY_WISH.getMessage(), UserResponseCode.WISH_EMPTY.getCode());
    }
}

package com.postgraduate.domain.member.user.exception;

import com.postgraduate.domain.member.user.presentation.constant.UserResponseCode;
import com.postgraduate.domain.member.user.presentation.constant.UserResponseMessage;
import com.postgraduate.global.exception.ApplicationException;

public class WishEmptyException extends ApplicationException {
    public WishEmptyException() {
        super(UserResponseMessage.EMPTY_WISH.getMessage(), UserResponseCode.WISH_EMPTY.getCode());
    }
}

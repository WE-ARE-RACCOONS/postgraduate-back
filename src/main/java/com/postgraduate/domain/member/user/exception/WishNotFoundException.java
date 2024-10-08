package com.postgraduate.domain.member.user.exception;

import com.postgraduate.domain.member.user.presentation.constant.UserResponseCode;
import com.postgraduate.domain.member.user.presentation.constant.UserResponseMessage;
import com.postgraduate.global.exception.ApplicationException;

public class WishNotFoundException extends ApplicationException {
    public WishNotFoundException() {
        super(UserResponseMessage.NOT_FOUND_WISH.getMessage(), UserResponseCode.WISH_NOT_FOUND.getCode());
    }
}

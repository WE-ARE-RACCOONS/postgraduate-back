package com.postgraduate.domain.user.wish.exception;

import com.postgraduate.domain.user.wish.presentation.constant.WishResponseCode;
import com.postgraduate.domain.user.wish.presentation.constant.WishResponseMessage;
import com.postgraduate.global.exception.ApplicationException;

public class WishNotFoundException extends ApplicationException {
    public WishNotFoundException() {
        super(WishResponseMessage.NOT_FOUND_WISH.getMessage(), WishResponseCode.WISH_NOT_FOUND.getCode());
    }
}

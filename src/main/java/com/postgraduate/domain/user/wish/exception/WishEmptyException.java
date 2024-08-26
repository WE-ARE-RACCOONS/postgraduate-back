package com.postgraduate.domain.user.wish.exception;

import com.postgraduate.domain.user.wish.presentation.constant.WishResponseCode;
import com.postgraduate.domain.user.wish.presentation.constant.WishResponseMessage;
import com.postgraduate.global.exception.ApplicationException;

public class WishEmptyException extends ApplicationException {
    public WishEmptyException() {
        super(WishResponseMessage.EMPTY_WISH.getMessage(), WishResponseCode.WISH_EMPTY.getCode());
    }
}

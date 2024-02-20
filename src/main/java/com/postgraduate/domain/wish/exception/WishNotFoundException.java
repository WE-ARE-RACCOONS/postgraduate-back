package com.postgraduate.domain.wish.exception;

import com.postgraduate.global.exception.ApplicationException;

import static com.postgraduate.domain.wish.presentation.constant.WishResponseCode.WISH_NOT_FOUND;
import static com.postgraduate.domain.wish.presentation.constant.WishResponseMessage.NOT_FOUND_WISH;

public class WishNotFoundException extends ApplicationException {
    public WishNotFoundException() {
        super(NOT_FOUND_WISH.getMessage(), WISH_NOT_FOUND.getCode());
    }
}

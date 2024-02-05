package com.postgraduate.domain.wish.exception;

import com.postgraduate.global.exception.ApplicationException;

import static com.postgraduate.domain.wish.presentation.constant.WishResponseCode.WISH_EMPTY;
import static com.postgraduate.domain.wish.presentation.constant.WishResponseMessage.EMPTY_WISH;

public class WishEmptyException extends ApplicationException {
    public WishEmptyException() {
        super(EMPTY_WISH.getMessage(), WISH_EMPTY.getCode());
    }
}

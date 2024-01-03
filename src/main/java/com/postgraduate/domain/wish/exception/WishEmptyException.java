package com.postgraduate.domain.wish.exception;

import com.postgraduate.domain.admin.exception.SeniorException;

import static com.postgraduate.domain.wish.presentation.constant.WishResponseCode.WISH_EMPTY;
import static com.postgraduate.domain.wish.presentation.constant.WishResponseMessage.EMPTY_WISH;

public class WishEmptyException extends SeniorException {
    public WishEmptyException() {
        super(EMPTY_WISH.getMessage(), WISH_EMPTY.getCode());
    }
}

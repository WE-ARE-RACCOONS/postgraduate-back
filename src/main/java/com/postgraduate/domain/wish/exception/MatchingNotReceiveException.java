package com.postgraduate.domain.wish.exception;

import com.postgraduate.global.exception.ApplicationException;

import static com.postgraduate.domain.wish.presentation.constant.WishResponseCode.MATCHING_NOT_AGREE;
import static com.postgraduate.domain.wish.presentation.constant.WishResponseMessage.NOT_AGREE_MATCHING;

public class MatchingNotReceiveException extends ApplicationException {
    public MatchingNotReceiveException() {
        super(NOT_AGREE_MATCHING.getMessage(), MATCHING_NOT_AGREE.getCode());
    }
}

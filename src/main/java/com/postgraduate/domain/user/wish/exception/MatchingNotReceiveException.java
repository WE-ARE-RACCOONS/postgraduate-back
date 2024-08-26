package com.postgraduate.domain.user.wish.exception;

import com.postgraduate.domain.user.wish.presentation.constant.WishResponseCode;
import com.postgraduate.domain.user.wish.presentation.constant.WishResponseMessage;
import com.postgraduate.global.exception.ApplicationException;

public class MatchingNotReceiveException extends ApplicationException {
    public MatchingNotReceiveException() {
        super(WishResponseMessage.NOT_AGREE_MATCHING.getMessage(), WishResponseCode.MATCHING_NOT_AGREE.getCode());
    }
}

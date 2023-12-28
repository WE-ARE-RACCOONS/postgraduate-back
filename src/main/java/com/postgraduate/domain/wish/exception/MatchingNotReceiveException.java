package com.postgraduate.domain.wish.exception;

import com.postgraduate.domain.salary.exception.SalaryException;

import static com.postgraduate.domain.wish.presentation.constant.WishResponseCode.MATCHING_NOT_RECEIVE;
import static com.postgraduate.domain.wish.presentation.constant.WishResponseMessage.NOT_AGREE_MATCHING;

public class MatchingNotReceiveException extends SalaryException {
    public MatchingNotReceiveException() {
        super(NOT_AGREE_MATCHING.getMessage(), MATCHING_NOT_RECEIVE.getCode());
    }
}

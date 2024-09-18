package com.postgraduate.domain.member.user.exception;

import com.postgraduate.domain.member.user.presentation.constant.UserResponseCode;
import com.postgraduate.domain.member.user.presentation.constant.UserResponseMessage;
import com.postgraduate.global.exception.ApplicationException;

public class MatchingNotReceiveException extends ApplicationException {
    public MatchingNotReceiveException() {
        super(UserResponseMessage.NOT_AGREE_MATCHING.getMessage(), UserResponseCode.MATCHING_NOT_AGREE.getCode());
    }
}

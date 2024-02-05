package com.postgraduate.domain.mentoring.exception;

import com.postgraduate.domain.mentoring.presentation.constant.MentoringResponseCode;
import com.postgraduate.domain.mentoring.presentation.constant.MentoringResponseMessage;
import com.postgraduate.global.exception.ApplicationException;

public class MentoringDateException extends ApplicationException {
    public MentoringDateException() {
        super(MentoringResponseMessage.INVALID_DATE.getMessage(), MentoringResponseCode.INVALID_DATE.getCode());
    }
}

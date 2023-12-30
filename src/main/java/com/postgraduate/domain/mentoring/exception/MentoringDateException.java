package com.postgraduate.domain.mentoring.exception;

import com.postgraduate.domain.mentoring.presentation.constant.MentoringResponseCode;
import com.postgraduate.domain.mentoring.presentation.constant.MentoringResponseMessage;

public class MentoringDateException extends MentoringException {
    public MentoringDateException() {
        super(MentoringResponseMessage.INVALID_DATE.getMessage(), MentoringResponseCode.INVALID_DATE.getCode());
    }
}

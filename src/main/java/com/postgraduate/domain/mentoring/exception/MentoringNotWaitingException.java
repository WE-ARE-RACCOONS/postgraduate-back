package com.postgraduate.domain.mentoring.exception;

import com.postgraduate.global.exception.ApplicationException;

import static com.postgraduate.domain.mentoring.presentation.constant.MentoringResponseCode.MENTORING_NOT_WAITING;
import static com.postgraduate.domain.mentoring.presentation.constant.MentoringResponseMessage.NOT_WAITING_MENTORING;

public class MentoringNotWaitingException extends ApplicationException {

    public MentoringNotWaitingException() {
        super(NOT_WAITING_MENTORING.getMessage(), MENTORING_NOT_WAITING.getCode());
    }
}

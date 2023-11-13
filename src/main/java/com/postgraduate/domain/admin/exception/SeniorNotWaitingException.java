package com.postgraduate.domain.admin.exception;

import static com.postgraduate.domain.mentoring.presentation.constant.MentoringResponseCode.MENTORING_NOT_WAITING;
import static com.postgraduate.domain.mentoring.presentation.constant.MentoringResponseMessage.NOT_WAITING_MENTORING;

public class SeniorNotWaitingException extends SeniorException{
    public SeniorNotWaitingException() {
        super(NOT_WAITING_MENTORING.getMessage(), MENTORING_NOT_WAITING.getCode());
        //TODO: 메시지, 코드 변경
    }
}

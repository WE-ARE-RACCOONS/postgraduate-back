package com.postgraduate.domain.mentoring.exception;

import com.postgraduate.global.exception.ApplicationException;

import static com.postgraduate.domain.mentoring.presentation.constant.MentoringResponseCode.DETAIL_NOT_FOUND;
import static com.postgraduate.domain.mentoring.presentation.constant.MentoringResponseCode.MENTORING_PRESENT;
import static com.postgraduate.domain.mentoring.presentation.constant.MentoringResponseMessage.NOT_FOUND_DETAIL;
import static com.postgraduate.domain.mentoring.presentation.constant.MentoringResponseMessage.PRESENT_MENTORING;


public class MentoringPresentException extends ApplicationException {
    public MentoringPresentException() {
        super(PRESENT_MENTORING.getMessage(), MENTORING_PRESENT.getCode());
    }
}

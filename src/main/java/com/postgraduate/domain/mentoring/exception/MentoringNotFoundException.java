package com.postgraduate.domain.mentoring.exception;

import com.postgraduate.global.exception.ApplicationException;

import static com.postgraduate.domain.mentoring.presentation.constant.MentoringResponseCode.MENTORING_NOT_FOUND;
import static com.postgraduate.domain.mentoring.presentation.constant.MentoringResponseMessage.NOT_FOUND_MENTORING;


public class MentoringNotFoundException extends ApplicationException {

    public MentoringNotFoundException() {
        super(NOT_FOUND_MENTORING.getMessage(), MENTORING_NOT_FOUND.getCode());
    }
}

package com.postgraduate.domain.mentoring.exception;

import com.postgraduate.global.exception.ApplicationException;

import static com.postgraduate.domain.mentoring.presentation.constant.MentoringResponseCode.MENTORING_NOT_EXPECTED;
import static com.postgraduate.domain.mentoring.presentation.constant.MentoringResponseMessage.NOT_EXPECTED_MENTORING;

public class MentoringNotExpectedException extends ApplicationException {

    public MentoringNotExpectedException() {
        super(NOT_EXPECTED_MENTORING.getMessage(), MENTORING_NOT_EXPECTED.getCode());
    }
}

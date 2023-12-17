package com.postgraduate.domain.mentoring.exception;

import static com.postgraduate.domain.mentoring.presentation.constant.MentoringResponseCode.MENTORING_NOT_EXPECTED;
import static com.postgraduate.domain.mentoring.presentation.constant.MentoringResponseCode.MENTORING_NOT_WAITING;
import static com.postgraduate.domain.mentoring.presentation.constant.MentoringResponseMessage.NOT_EXPECTED_MENTORING;
import static com.postgraduate.domain.mentoring.presentation.constant.MentoringResponseMessage.NOT_WAITING_MENTORING;

public class MentoringNotExpectedException extends MentoringException {

    public MentoringNotExpectedException() {
        super(NOT_EXPECTED_MENTORING.getMessage(), MENTORING_NOT_EXPECTED.getCode());
    }
}

package com.postgraduate.domain.mentoring.exception;

import static com.postgraduate.domain.mentoring.presentation.constant.MentoringResponseCode.MENTORING_DONE;
import static com.postgraduate.domain.mentoring.presentation.constant.MentoringResponseMessage.DONE_MENTORING;


public class MentoringDoneException extends MentoringException {

    public MentoringDoneException() {
        super(DONE_MENTORING.getMessage(), MENTORING_DONE.getCode());
    }
}

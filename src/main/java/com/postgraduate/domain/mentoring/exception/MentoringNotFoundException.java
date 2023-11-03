package com.postgraduate.domain.mentoring.exception;

import static com.postgraduate.domain.mentoring.presentation.constant.MentoringResponseCode.MENTORING_NOT_FOUND;
import static com.postgraduate.domain.mentoring.presentation.constant.MentoringResponseMessage.NOT_FOUND_MENTORING;
import static org.springframework.http.HttpStatus.NOT_FOUND;


public class MentoringNotFoundException extends MentoringException {

    public MentoringNotFoundException() {
        super(NOT_FOUND_MENTORING.getMessage(), MENTORING_NOT_FOUND.getCode(), NOT_FOUND);
    }
}

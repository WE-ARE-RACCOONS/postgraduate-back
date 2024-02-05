package com.postgraduate.domain.mentoring.exception;

import com.postgraduate.global.exception.ApplicationException;

import static com.postgraduate.domain.mentoring.presentation.constant.MentoringResponseCode.DETAIL_NOT_FOUND;
import static com.postgraduate.domain.mentoring.presentation.constant.MentoringResponseMessage.NOT_FOUND_DETAIL;


public class MentoringDetailNotFoundException extends ApplicationException {
    public MentoringDetailNotFoundException() {
        super(NOT_FOUND_DETAIL.getMessage(), DETAIL_NOT_FOUND.getCode());
    }
}

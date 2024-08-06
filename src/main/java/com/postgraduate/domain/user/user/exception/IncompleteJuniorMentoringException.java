package com.postgraduate.domain.user.user.exception;

import com.postgraduate.global.exception.ApplicationException;

public class IncompleteJuniorMentoringException extends ApplicationException {
    public IncompleteJuniorMentoringException() {
        super("신청한 멘토링 중 완료되지 않은 멘토링이 있습니다", "code");
    }
}

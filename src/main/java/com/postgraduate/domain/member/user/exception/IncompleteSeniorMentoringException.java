package com.postgraduate.domain.member.user.exception;

import com.postgraduate.global.exception.ApplicationException;

public class IncompleteSeniorMentoringException extends ApplicationException {
    public IncompleteSeniorMentoringException() {
        super("신청받은 멘토링 중 완료되지 않은 멘토링이 있습니다", "code");
    }
}

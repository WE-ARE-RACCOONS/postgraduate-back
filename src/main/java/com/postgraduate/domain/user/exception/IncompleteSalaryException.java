package com.postgraduate.domain.user.exception;

import com.postgraduate.global.exception.ApplicationException;

public class IncompleteSalaryException extends ApplicationException {
    public IncompleteSalaryException() {
        super("아직 정산이 완료되지 않았습니다", "code");
    }
}

package com.postgraduate.domain.salary.exception;

import com.postgraduate.global.exception.ApplicationException;

public class SalaryNotYetException extends ApplicationException {
    public SalaryNotYetException() {
        super("정산이 필요하지 않습니다.", "code");
    }
}

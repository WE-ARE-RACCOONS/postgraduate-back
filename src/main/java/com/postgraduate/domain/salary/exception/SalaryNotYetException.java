package com.postgraduate.domain.salary.exception;

import com.postgraduate.global.exception.ApplicationException;

import static com.postgraduate.domain.salary.presentation.constant.SalaryResponseCode.SALARY_NOT_NEED;
import static com.postgraduate.domain.salary.presentation.constant.SalaryResponseMessage.NOT_NEED_SALARY;

public class SalaryNotYetException extends ApplicationException {
    public SalaryNotYetException() {
        super(NOT_NEED_SALARY.getMessage(), SALARY_NOT_NEED.getCode());
    }
}

package com.postgraduate.domain.senior.salary.exception;

import com.postgraduate.global.exception.ApplicationException;

import static com.postgraduate.domain.senior.salary.presentation.constant.SalaryResponseCode.SALARY_NOT_FOUND;
import static com.postgraduate.domain.senior.salary.presentation.constant.SalaryResponseMessage.NOT_FOUND_SALARY;

public class SalaryNotFoundException extends ApplicationException {
    public SalaryNotFoundException() {
        super(NOT_FOUND_SALARY.getMessage(), SALARY_NOT_FOUND.getCode());
    }
}

package com.postgraduate.domain.salary.exception;

import static com.postgraduate.domain.salary.presentation.constant.SalaryResponseCode.SALARY_NOT_FOUND;
import static com.postgraduate.domain.salary.presentation.constant.SalaryResponseMessage.NOT_FOUND_SALARY;

public class SalaryNotFoundException extends SalaryException {
    public SalaryNotFoundException() {
        super(NOT_FOUND_SALARY.getMessage(), SALARY_NOT_FOUND.getCode());
    }
}

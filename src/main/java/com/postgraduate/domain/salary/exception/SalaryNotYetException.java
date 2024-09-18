package com.postgraduate.domain.salary.exception;

import com.postgraduate.domain.salary.presentation.constant.SalaryResponseCode;
import com.postgraduate.domain.salary.presentation.constant.SalaryResponseMessage;
import com.postgraduate.global.exception.ApplicationException;

public class SalaryNotYetException extends ApplicationException {
    public SalaryNotYetException() {
        super(SalaryResponseMessage.NOT_NEED_SALARY.getMessage(), SalaryResponseCode.SALARY_NOT_NEED.getCode());
    }
}

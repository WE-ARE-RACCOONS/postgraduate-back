package com.postgraduate.domain.salary.presentation.constant;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
public enum SalaryResponseCode {
    SALARY_FIND("SLR200"),
    SALARY_UPDATE("SLR201"),
    SALARY_CREATE("SLR202"),
    SALARY_DELETE("SLR203"),

    SALARY_NOT_FOUND("EX500");
    private final String code;
}

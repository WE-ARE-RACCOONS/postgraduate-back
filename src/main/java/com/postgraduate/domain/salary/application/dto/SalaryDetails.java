package com.postgraduate.domain.salary.application.dto;

import java.time.LocalDate;

public record SalaryDetails(
        String profile,
        String nickName,
        String date,
        int term,
        int salaryAmount,
        LocalDate salaryDate
) { }

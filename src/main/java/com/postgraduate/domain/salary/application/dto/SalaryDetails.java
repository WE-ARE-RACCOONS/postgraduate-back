package com.postgraduate.domain.salary.application.dto;

import jakarta.validation.constraints.NotNull;

import java.time.LocalDate;

public record SalaryDetails(
        @NotNull
        String profile,
        @NotNull
        String nickName,
        @NotNull
        String date,
        @NotNull
        int term,
        @NotNull
        int salaryAmount,
        @NotNull
        LocalDate salaryDate
) { }

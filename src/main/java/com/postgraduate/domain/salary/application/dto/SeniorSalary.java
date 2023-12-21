package com.postgraduate.domain.salary.application.dto;

import com.postgraduate.domain.senior.domain.entity.Senior;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDate;

public record SeniorSalary(
        @NotNull
        Senior senior,
        @NotNull
        LocalDate salaryDate
) {
}

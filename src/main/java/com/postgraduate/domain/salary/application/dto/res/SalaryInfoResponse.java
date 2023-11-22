package com.postgraduate.domain.salary.application.dto.res;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDate;

@Getter
@Builder
@AllArgsConstructor
public class SalaryInfoResponse {
    private LocalDate salaryDate;
    private int salaryAmount;
}

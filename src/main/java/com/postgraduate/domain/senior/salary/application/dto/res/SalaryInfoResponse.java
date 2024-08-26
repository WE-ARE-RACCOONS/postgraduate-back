package com.postgraduate.domain.senior.salary.application.dto.res;

import java.time.LocalDate;

public record SalaryInfoResponse(LocalDate salaryDate, int salaryAmount) {
}

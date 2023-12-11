package com.postgraduate.domain.salary.application.dto.res;

import com.postgraduate.domain.salary.application.dto.SalaryDetails;
import jakarta.validation.constraints.NotNull;

import java.util.List;

public record SalaryDetailsResponse(@NotNull List<SalaryDetails> salaryDetails) {
}

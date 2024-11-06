package com.postgraduate.domain.salary.application.dto.res;

import com.postgraduate.domain.salary.application.dto.SalaryDetails;

import java.util.List;

public record SalaryDetailsResponse(List<SalaryDetails> salaryDetails) {
}

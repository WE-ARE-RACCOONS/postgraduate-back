package com.postgraduate.domain.senior.salary.application.dto.res;

import com.postgraduate.domain.senior.salary.application.dto.SalaryDetails;

import java.util.List;

public record SalaryDetailsResponse(List<SalaryDetails> salaryDetails) {
}

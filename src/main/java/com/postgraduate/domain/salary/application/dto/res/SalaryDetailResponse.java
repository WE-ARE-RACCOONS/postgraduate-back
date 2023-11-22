package com.postgraduate.domain.salary.application.dto.res;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDate;

@Getter
@Builder
@AllArgsConstructor
public class SalaryDetailResponse {
    private String profile;
    private String nickName;
    private String date;
    private int term;
    private int salaryAmount;
    private LocalDate salaryDate;
}

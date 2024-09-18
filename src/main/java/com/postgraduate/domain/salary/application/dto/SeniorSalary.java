package com.postgraduate.domain.salary.application.dto;

import com.postgraduate.domain.member.senior.domain.entity.Senior;

import java.time.LocalDate;

public record SeniorSalary(
        Senior senior,
        LocalDate salaryDate
) {}

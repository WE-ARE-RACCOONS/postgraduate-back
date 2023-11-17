package com.postgraduate.domain.salary.application.mapper;

import com.postgraduate.domain.salary.domain.entity.Salary;
import com.postgraduate.domain.senior.domain.entity.Senior;

public class SalaryMapper {
    public static Salary mapToSalary(String month, Senior senior) {
        return Salary.builder()
                .month(month)
                .senior(senior)
                .build();
    }
}

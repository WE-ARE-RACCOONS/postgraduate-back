package com.postgraduate.domain.salary.application.mapper;

import com.postgraduate.domain.mentoring.domain.entity.Mentoring;
import com.postgraduate.domain.salary.application.dto.SalaryDetails;
import com.postgraduate.domain.salary.domain.entity.Salary;
import com.postgraduate.domain.user.domain.entity.User;

import java.time.LocalDate;

public class SalaryMapper {
    public static Salary mapToSalary(Mentoring mentoring, LocalDate salaryDate) {
        return Salary.builder()
                .senior(mentoring.getSenior())
                .mentoring(mentoring)
                .salaryDate(salaryDate)
                .build();
    }

    public static SalaryDetails mapToSalaryDetail(Salary salary) {
        Mentoring mentoring = salary.getMentoring();
        User user = mentoring.getUser();
        return new SalaryDetails(
                user.getProfile(),
                user.getNickName(),
                mentoring.getDate(),
                salary.getSenior().getProfile().getTerm(),
                mentoring.getPay(),
                salary.getSalaryDate()
        );
    }
}

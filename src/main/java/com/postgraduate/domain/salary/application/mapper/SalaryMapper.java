package com.postgraduate.domain.salary.application.mapper;

import com.postgraduate.domain.mentoring.domain.entity.Mentoring;
import com.postgraduate.domain.salary.application.dto.res.SalaryDetailResponse;
import com.postgraduate.domain.salary.domain.entity.Salary;

import java.time.LocalDate;

public class SalaryMapper {
    public static Salary mapToSalary(Mentoring mentoring, LocalDate salaryDate) {
        return Salary.builder()
                .senior(mentoring.getSenior())
                .mentoring(mentoring)
                .salaryDate(salaryDate)
                .build();
    }

    public static SalaryDetailResponse mapToSalaryDetail(Salary salary) {
        return SalaryDetailResponse.builder()
                .profile(salary.getMentoring().getUser().getProfile())
                .nickName(salary.getMentoring().getUser().getNickName())
                .date(salary.getMentoring().getDate())
                .term(salary.getSenior().getProfile().getTerm())
                .pay(salary.getMentoring().getPay()) //TODO 수수료
                .salaryDate(salary.getSalaryDate())
                .build();
    }
}

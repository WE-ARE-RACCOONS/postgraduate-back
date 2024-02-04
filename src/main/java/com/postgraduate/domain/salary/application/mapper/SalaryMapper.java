package com.postgraduate.domain.salary.application.mapper;

import com.postgraduate.domain.account.domain.entity.Account;
import com.postgraduate.domain.mentoring.domain.entity.Mentoring;
import com.postgraduate.domain.payment.domain.entity.Payment;
import com.postgraduate.domain.salary.application.dto.SalaryDetails;
import com.postgraduate.domain.salary.domain.entity.Salary;
import com.postgraduate.domain.senior.domain.entity.Senior;
import com.postgraduate.domain.user.domain.entity.User;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public class SalaryMapper {
    private static final int AMOUNT = 20000;
    public static Salary mapToSalary(Senior senior, LocalDate salaryDate) {
        return Salary.builder()
                .senior(senior)
                .salaryDate(salaryDate)
                .build();
    }

    public static Salary mapToSalary(Senior senior, LocalDate salaryDate, Account account) {
        if (account == null)
            return mapToSalary(senior, salaryDate);
        return Salary.builder()
                .senior(senior)
                .salaryDate(salaryDate)
                .accountHolder(account.getAccountHolder())
                .accountNumber(account.getAccountNumber())
                .bank(account.getBank())
                .build();
    }

    public static SalaryDetails mapToSalaryDetail(Salary salary, Mentoring mentoring) {
        User user = mentoring.getUser();
        return new SalaryDetails(
                user.getProfile(),
                user.getNickName(),
                mentoring.getDate(),
                mentoring.getTerm(),
                AMOUNT,
                salary.getSalaryDate()
        );
    }
}

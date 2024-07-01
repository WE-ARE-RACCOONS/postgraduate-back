package com.postgraduate.domain.salary.application.mapper;

import com.postgraduate.domain.account.domain.entity.Account;
import com.postgraduate.domain.mentoring.domain.entity.Mentoring;
import com.postgraduate.domain.salary.application.dto.SalaryDetails;
import com.postgraduate.domain.salary.domain.entity.Salary;
import com.postgraduate.domain.salary.domain.entity.SalaryAccount;
import com.postgraduate.domain.senior.domain.entity.Senior;
import com.postgraduate.domain.user.application.utils.UserUtils;
import com.postgraduate.domain.user.domain.entity.User;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDate;

public class SalaryMapper {
    private SalaryMapper() {
        throw new IllegalArgumentException();
    }

    private static UserUtils userUtils = new UserUtils();

    public static Salary mapToSalary(Senior senior, LocalDate salaryDate) {
        return Salary.builder()
                .senior(senior)
                .salaryDate(salaryDate)
                .build();
    }

    public static SalaryDetails mapToSalaryDetail(Mentoring mentoring) {
        Salary salary = mentoring.getSalary();
        if (mentoring.getUser() == null || mentoring.getUser().isDelete()){
            User user = userUtils.getArchiveUser();
            return getSalaryDetails(mentoring, salary, user);
        }
        User user = mentoring.getUser();
        return getSalaryDetails(mentoring, salary, user);
    }

    @NotNull
    private static SalaryDetails getSalaryDetails(Mentoring mentoring, Salary salary, User user) {
        return new SalaryDetails(
                user.getProfile(),
                user.getNickName(),
                mentoring.getDate(),
                mentoring.getTerm(),
                mentoring.calculateForSenior(),
                salary.getSalaryDate()
        );
    }

    public static SalaryAccount mapToSalaryAccount(Account account) {
        return new SalaryAccount(
                account.getBank(),
                account.getAccountNumber(),
                account.getAccountHolder()
        );
    }
}

package com.postgraduate.domain.salary.application.mapper;

import com.postgraduate.domain.account.domain.entity.Account;
import com.postgraduate.domain.mentoring.domain.entity.Mentoring;
import com.postgraduate.domain.salary.application.dto.SalaryDetails;
import com.postgraduate.domain.salary.domain.entity.Salary;
import com.postgraduate.domain.salary.domain.entity.SalaryAccount;
import com.postgraduate.domain.senior.domain.entity.Senior;
import com.postgraduate.domain.user.user.application.utils.UserUtils;
import com.postgraduate.domain.user.user.domain.entity.User;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.time.LocalDate;

@RequiredArgsConstructor
@Component
public class SalaryMapper {
    private final UserUtils userUtils;

    public Salary mapToSalary(Senior senior, LocalDate salaryDate) {
        return Salary.builder()
                .senior(senior)
                .salaryDate(salaryDate)
                .build();
    }

    public SalaryDetails mapToSalaryDetail(Mentoring mentoring) {
        Salary salary = mentoring.getSalary();
        if (mentoring.getUser() == null || mentoring.getUser().isDelete()){
            User user = userUtils.getArchiveUser();
            return getSalaryDetails(mentoring, salary, user);
        }
        User user = mentoring.getUser();
        return getSalaryDetails(mentoring, salary, user);
    }

    @NotNull
    private SalaryDetails getSalaryDetails(Mentoring mentoring, Salary salary, User user) {
        return new SalaryDetails(
                user.getProfile(),
                user.getNickName(),
                mentoring.getDate(),
                mentoring.getTerm(),
                mentoring.calculateForSenior(),
                salary.getSalaryDate()
        );
    }

    public SalaryAccount mapToSalaryAccount(Account account) {
        return new SalaryAccount(
                account.getBank(),
                account.getAccountNumber(),
                account.getAccountHolder()
        );
    }
}

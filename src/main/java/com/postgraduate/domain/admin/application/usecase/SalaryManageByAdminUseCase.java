package com.postgraduate.domain.admin.application.usecase;

import com.postgraduate.domain.account.domain.entity.Account;
import com.postgraduate.domain.account.domain.service.AccountGetService;
import com.postgraduate.domain.account.exception.AccountNotFoundException;
import com.postgraduate.domain.admin.application.dto.res.SalaryResponse;
import com.postgraduate.domain.admin.application.mapper.AdminMapper;
import com.postgraduate.domain.salary.domain.entity.Salary;
import com.postgraduate.domain.salary.domain.service.SalaryGetService;
import com.postgraduate.domain.senior.domain.entity.Senior;
import com.postgraduate.domain.senior.domain.service.SeniorGetService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

import static com.postgraduate.domain.salary.util.SalaryUtil.*;

@Service
@RequiredArgsConstructor
public class SalaryManageByAdminUseCase {
    private final SeniorGetService seniorGetService;
    private final AccountGetService accountGetService;
    private final SalaryGetService salaryGetService;

    public SalaryResponse getSalary(Long seniorId) {
        Senior senior = seniorGetService.bySeniorId(seniorId);
        Account account = accountGetService.bySenior(senior).orElseThrow(AccountNotFoundException::new);
        List<Salary> salaries = salaryGetService.bySeniorAndSalaryDate(senior, getSalaryDate());
        int totalAmount = getAmount(salaries);
        Boolean status = getStatus(salaries);
        return AdminMapper.mapToSalaryResponse(account, totalAmount, status);
    }
}

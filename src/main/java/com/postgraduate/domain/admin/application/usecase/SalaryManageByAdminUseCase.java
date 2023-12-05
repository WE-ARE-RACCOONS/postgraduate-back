package com.postgraduate.domain.admin.application.usecase;

import com.postgraduate.domain.account.domain.entity.Account;
import com.postgraduate.domain.account.domain.service.AccountGetService;
import com.postgraduate.domain.account.util.AccountUtil;
import com.postgraduate.domain.admin.application.dto.res.AllSalariesResponse;
import com.postgraduate.domain.admin.application.dto.res.SalariesResponse;
import com.postgraduate.domain.admin.application.dto.res.SalaryDetailsResponse;
import com.postgraduate.domain.admin.application.mapper.AdminMapper;
import com.postgraduate.domain.salary.domain.entity.Salary;
import com.postgraduate.domain.salary.domain.entity.constant.SalaryStatus;
import com.postgraduate.domain.salary.domain.service.SalaryGetService;
import com.postgraduate.domain.senior.domain.entity.Senior;
import com.postgraduate.domain.senior.domain.service.SeniorGetService;
import com.postgraduate.global.config.security.util.EncryptorUtils;
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
    private final EncryptorUtils encryptorUtils;
    private final AccountUtil accountUtil;

    public SalaryDetailsResponse getSalary(Long seniorId) {
        Senior senior = seniorGetService.bySeniorId(seniorId);
        Account account = accountGetService.bySenior(senior).orElse(accountUtil.createDummyAccount());
        String accountNumber = encryptorUtils.decryptData(account.getAccountNumber());
        List<Salary> salaries = salaryGetService.bySeniorAndSalaryDate(senior, getSalaryDate());
        int totalAmount = getAmount(salaries);
        SalaryStatus status = getStatus(salaries);
        return AdminMapper.mapToSalaryResponse(senior, account, accountNumber, totalAmount, status);
    }
}

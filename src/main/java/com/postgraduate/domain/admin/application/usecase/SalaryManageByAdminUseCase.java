package com.postgraduate.domain.admin.application.usecase;

import com.postgraduate.domain.account.domain.entity.Account;
import com.postgraduate.domain.account.domain.service.AccountGetService;
import com.postgraduate.domain.admin.application.dto.res.SalaryManageResponse;
import com.postgraduate.domain.admin.application.dto.SalaryInfo;
import com.postgraduate.domain.admin.application.dto.res.SalaryDetailsResponse;
import com.postgraduate.domain.admin.application.mapper.AdminMapper;
import com.postgraduate.domain.salary.domain.entity.Salary;
import com.postgraduate.domain.salary.domain.entity.constant.SalaryStatus;
import com.postgraduate.domain.salary.domain.service.SalaryGetService;
import com.postgraduate.domain.salary.domain.service.SalaryUpdateService;
import com.postgraduate.domain.senior.domain.entity.Senior;
import com.postgraduate.domain.senior.domain.service.SeniorGetService;
import com.postgraduate.global.config.security.util.EncryptorUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static com.postgraduate.domain.salary.domain.entity.constant.SalaryStatus.DONE;
import static com.postgraduate.domain.salary.util.SalaryUtil.*;

@Service
@Transactional
@RequiredArgsConstructor
public class SalaryManageByAdminUseCase {
    private final SeniorGetService seniorGetService;
    private final AccountGetService accountGetService;
    private final SalaryGetService salaryGetService;
    private final EncryptorUtils encryptorUtils;
    private final SalaryUpdateService salaryUpdateService;

    public SalaryDetailsResponse getSalary(Long seniorId) {
        Senior senior = seniorGetService.bySeniorId(seniorId);
        List<Salary> salaries = salaryGetService.bySeniorAndSalaryDate(senior, getSalaryDate());
        int totalAmount = getAmount(salaries);
        SalaryStatus status = getStatus(salaries);

        Optional<Account> optionalAccount = accountGetService.bySenior(senior);
        if (optionalAccount.isPresent()) {
            Account account = optionalAccount.get();
            String accountNumber = encryptorUtils.decryptData(account.getAccountNumber());
            return AdminMapper.mapToSalaryDetailsResponse(senior, account, accountNumber, totalAmount, status);
        }
        return AdminMapper.mapToSalaryDetailsResponse(senior, totalAmount, status);
    }

    public SalaryManageResponse getSalaries() {
        List<SalaryInfo> responses = new ArrayList<>();
        List<Senior> seniors = seniorGetService.getAll();
        for (Senior senior : seniors) {
            List<Salary> salaries = salaryGetService.bySeniorAndSalaryDateAndStatus(senior, getSalaryDate(), true);
            if (getStatus(salaries) != DONE) {
                continue;
            }
            SalaryInfo response = getSalaryInfo(senior, salaries);
            responses.add(response);
        }
        return new SalaryManageResponse(responses);
    }

    private SalaryInfo getSalaryInfo(Senior senior, List<Salary> salaries) {
        int totalAmount = getAmount(salaries);
        LocalDateTime salaryDoneDate = getDoneDate(salaries);

        Optional<Account> optionalAccount = accountGetService.bySenior(senior);
        if (optionalAccount.isPresent()) {
            Account account = optionalAccount.get();
            String accountNumber = encryptorUtils.decryptData(account.getAccountNumber());
            return AdminMapper.mapToSalaryResponse(senior, account, accountNumber, totalAmount, salaryDoneDate);
        }
        return AdminMapper.mapToSalaryResponse(senior, totalAmount, salaryDoneDate);
    }

    public void updateSalaryStatus(Long seniorId, Boolean status) {
        Senior senior = seniorGetService.bySeniorId(seniorId);
        List<Salary> salaries = salaryGetService.bySeniorAndSalaryDate(senior, getSalaryDate());
        for (Salary salary : salaries) {
            salaryUpdateService.updateStatus(salary, status);
        }
    }
}

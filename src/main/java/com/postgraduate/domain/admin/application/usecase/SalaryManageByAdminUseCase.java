package com.postgraduate.domain.admin.application.usecase;

import com.postgraduate.domain.account.domain.entity.Account;
import com.postgraduate.domain.account.domain.service.AccountGetService;
import com.postgraduate.domain.admin.application.dto.SalaryInfo;
import com.postgraduate.domain.admin.application.dto.res.SalaryDetailsResponse;
import com.postgraduate.domain.admin.application.dto.res.SalaryManageResponse;
import com.postgraduate.domain.admin.application.mapper.AdminMapper;
import com.postgraduate.domain.salary.application.dto.SeniorSalary;
import com.postgraduate.domain.salary.domain.entity.Salary;
import com.postgraduate.domain.salary.domain.entity.SalaryAccount;
import com.postgraduate.domain.salary.domain.service.SalaryGetService;
import com.postgraduate.domain.salary.domain.service.SalaryUpdateService;
import com.postgraduate.domain.senior.domain.entity.Senior;
import com.postgraduate.domain.senior.domain.service.SeniorGetService;
import com.postgraduate.global.config.security.util.EncryptorUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

import static com.postgraduate.domain.admin.application.mapper.AdminMapper.mapToSalaryResponse;
import static java.lang.Boolean.TRUE;

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
        Salary salary = salaryGetService.bySenior(senior);

        Optional<Account> optionalAccount = accountGetService.bySenior(senior);
        if (optionalAccount.isPresent()) {
            Account account = optionalAccount.get();
            String accountNumber = encryptorUtils.decryptData(account.getAccountNumber());
            return AdminMapper.mapToSalaryDetailsResponse(senior, account, accountNumber, salary.getTotalAmount(), salary.status());
        }
        return AdminMapper.mapToSalaryDetailsResponse(senior, salary.getTotalAmount(), salary.status());
    }

    public SalaryManageResponse getSalaries(Integer page, String search) {
        Page<SeniorSalary> seniorSalaries = salaryGetService.findDistinctSeniors(search, page);
        List<SalaryInfo> salaryInfos = seniorSalaries.stream()
                .map(seniorSalary -> {
                    Salary salary = salaryGetService.bySenior(seniorSalary.senior());
                    return getSalaryInfo(seniorSalary.senior(), salary);
                })
                .toList();
        Long totalElements = seniorSalaries.getTotalElements();
        int totalPages = seniorSalaries.getTotalPages();
        return new SalaryManageResponse(salaryInfos, totalElements, totalPages);
    }

    private SalaryInfo getSalaryInfo(Senior senior, Salary salary) {
        SalaryAccount account = salary.getAccount();
        if (account != null) {
            String accountNumber = encryptorUtils.decryptData(account.getAccountNumber());
            return mapToSalaryResponse(senior, accountNumber, salary);
        }
        return mapToSalaryResponse(senior, salary);
    }

    public void updateSalaryStatus(Long seniorId, Boolean status) {
        Senior senior = seniorGetService.bySeniorId(seniorId);
        Salary salary = salaryGetService.bySenior(senior);
        if (TRUE.equals(status)) {
            salaryUpdateService.updateDone(salary);
            return;
        }
        salaryUpdateService.updateNot(salary);
    }
}

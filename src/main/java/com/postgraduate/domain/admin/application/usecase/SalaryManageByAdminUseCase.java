package com.postgraduate.domain.admin.application.usecase;

import com.postgraduate.domain.account.domain.entity.Account;
import com.postgraduate.domain.account.domain.service.AccountGetService;
import com.postgraduate.domain.admin.application.dto.res.SalaryManageResponse;
import com.postgraduate.domain.admin.application.dto.SalaryInfo;
import com.postgraduate.domain.admin.application.dto.res.SalaryDetailsResponse;
import com.postgraduate.domain.admin.application.mapper.AdminMapper;
import com.postgraduate.domain.salary.domain.entity.Salary;
import com.postgraduate.domain.admin.presentation.constant.SalaryStatus;
import com.postgraduate.domain.salary.application.dto.SeniorSalary;
import com.postgraduate.domain.salary.domain.service.SalaryGetService;
import com.postgraduate.domain.salary.domain.service.SalaryUpdateService;
import com.postgraduate.domain.senior.domain.entity.Senior;
import com.postgraduate.domain.senior.domain.service.SeniorGetService;
import com.postgraduate.global.config.security.util.EncryptorUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

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
        Salary salary = salaryGetService.bySenior(senior);

        Optional<Account> optionalAccount = accountGetService.bySenior(senior);
        if (optionalAccount.isPresent()) {
            Account account = optionalAccount.get();
            String accountNumber = encryptorUtils.decryptData(account.getAccountNumber());
            return AdminMapper.mapToSalaryDetailsResponse(senior, account, accountNumber, salary.getTotalAmount(), salary.getStatus());
        }
        return AdminMapper.mapToSalaryDetailsResponse(senior, salary.getTotalAmount(), salary.getStatus());
    }

    public SalaryManageResponse getSalaries(Integer page, String search) {
        Page<SeniorSalary> seniors = salaryGetService.findDistinctSeniors(search, page);
        List<SalaryInfo> salaryInfos = seniors.stream()
                .map(senior -> {
                    Salary salary = salaryGetService.bySenior(senior.senior());
                    return getSalaryInfo(senior.senior(), salary);
                })
                .toList();
        Long totalElements = seniors.getTotalElements();
        int totalPages = seniors.getTotalPages();
        return new SalaryManageResponse(salaryInfos, totalElements, totalPages);
    }

    private SalaryInfo getSalaryInfo(Senior senior, Salary salary) {
        Optional<Account> optionalAccount = accountGetService.bySenior(senior);
        if (optionalAccount.isPresent()) {
            String accountNumber = encryptorUtils.decryptData(salary.getAccountNumber());
            return AdminMapper.mapToSalaryResponse(senior, accountNumber, salary);
        }
        return AdminMapper.mapToSalaryResponse(senior, salary);
    }

    public void updateSalaryStatus(Long seniorId, Boolean status) {
        Senior senior = seniorGetService.bySeniorId(seniorId);
        Salary salary = salaryGetService.bySenior(senior);
        salaryUpdateService.updateStatus(salary, status);
    }
}

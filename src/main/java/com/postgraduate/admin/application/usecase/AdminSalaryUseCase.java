package com.postgraduate.admin.application.usecase;

import com.postgraduate.admin.application.dto.res.SalaryInfoWithOutId;
import com.postgraduate.admin.application.dto.res.UnSettledSalaryInfo;
import com.postgraduate.admin.application.mapper.AdminMapper;
import com.postgraduate.domain.salary.domain.entity.Salary;
import com.postgraduate.domain.salary.domain.entity.SalaryAccount;
import com.postgraduate.domain.salary.domain.service.SalaryGetService;
import com.postgraduate.domain.salary.domain.service.SalaryUpdateService;
import com.postgraduate.domain.senior.domain.entity.Senior;
import com.postgraduate.domain.senior.domain.service.SeniorGetService;
import com.postgraduate.global.config.security.util.EncryptorUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
public class AdminSalaryUseCase {
    private final SeniorGetService seniorGetService;
    private final SalaryGetService salaryGetService;
    private final SalaryUpdateService salaryUpdateService;
    private final EncryptorUtils encryptorUtils;
    private final AdminMapper adminMapper;

    @Transactional(readOnly = true)
    public List<SalaryInfoWithOutId> salaryInfos() {
        List<Salary> all = salaryGetService.findAll();
        return all.stream()
                .map(salary -> {
                    if (salary.getSenior() == null)
                        return adminMapper.mapToSalaryResponse(salary);
                    SalaryAccount account = salary.getAccount();
                    String accountNumber = encryptorUtils.decryptData(account.getAccountNumber());
                    return adminMapper.mapToSalaryResponse(salary.getSenior(), accountNumber, salary);
                })
                .toList();
    }

    @Transactional(readOnly = true)
    public SalaryInfoWithOutId seniorSalary(Long seniorId) {
        Senior senior = seniorGetService.bySeniorId(seniorId);
        Salary salary = salaryGetService.bySenior(senior);
        SalaryAccount account = salary.getAccount();
        if (account == null)
            return adminMapper.mapToSalaryResponse(senior, salary);
        String accountNumber = encryptorUtils.decryptData(account.getAccountNumber());
        return adminMapper.mapToSalaryResponse(senior, accountNumber, salary);
    }

    public void salaryDone(Long salaryId) {
        Salary salary = salaryGetService.bySalaryId(salaryId);
        salaryUpdateService.updateDone(salary);
    }

    @Transactional(readOnly = true)
    public List<UnSettledSalaryInfo> unSettledSalaryInfo() {
        List<Salary> salaries = salaryGetService.allByNotDone();
        return salaries.stream()
                .map(salary -> {
                    SalaryAccount account = salary.getAccount();
                    if (account == null)
                        return adminMapper.mapToUnSettledSalaryResponse(salary);
                    String accountNumber = encryptorUtils.decryptData(account.getAccountNumber());
                    return adminMapper.mapToUnSettledSalaryResponse(salary, accountNumber);
                })
                .toList();
    }
}

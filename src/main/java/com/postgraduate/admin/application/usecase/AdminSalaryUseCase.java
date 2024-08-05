package com.postgraduate.admin.application.usecase;

import com.postgraduate.admin.application.dto.res.SalaryInfoWithOutId;
import com.postgraduate.admin.application.dto.res.UnSettledSalaryInfo;
import com.postgraduate.admin.application.mapper.AdminMapper;
import com.postgraduate.admin.domain.service.AdminSalaryService;
import com.postgraduate.domain.salary.domain.entity.Salary;
import com.postgraduate.domain.salary.domain.entity.SalaryAccount;
import com.postgraduate.global.config.security.util.EncryptorUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
public class AdminSalaryUseCase {
    private final EncryptorUtils encryptorUtils;
    private final AdminSalaryService adminSalaryService;
    private final AdminMapper adminMapper;

    @Transactional(readOnly = true)
    public List<SalaryInfoWithOutId> salaryInfos() {
        List<Salary> all = adminSalaryService.findAllDoneSalary();
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
        Salary salary = adminSalaryService.findBySeniorId(seniorId);
        SalaryAccount account = salary.getAccount();
        if (account == null)
            return adminMapper.mapToSalaryResponse(salary.getSenior(), salary);
        String accountNumber = encryptorUtils.decryptData(account.getAccountNumber());
        return adminMapper.mapToSalaryResponse(salary.getSenior(), accountNumber, salary);
    }

    public void salaryDone(Long salaryId) {
        adminSalaryService.updateDone(salaryId);
    }

    @Transactional(readOnly = true)
    public List<UnSettledSalaryInfo> unSettledSalaryInfo() {
        List<Salary> salaries = adminSalaryService.findAllByNotDone();
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

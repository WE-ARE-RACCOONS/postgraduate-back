package com.postgraduate.admin.application.usecase;

import com.postgraduate.admin.application.dto.res.SalaryInfos;
import com.postgraduate.admin.application.dto.res.UnsettledSalaryInfos;
import com.postgraduate.admin.application.mapper.AdminMapper;
import com.postgraduate.admin.domain.service.AdminSalaryService;
import com.postgraduate.domain.salary.domain.entity.Salary;
import com.postgraduate.domain.salary.domain.entity.SalaryAccount;
import com.postgraduate.global.config.security.util.EncryptorUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@RequiredArgsConstructor
public class AdminSalaryUseCase {
    private final EncryptorUtils encryptorUtils;
    private final AdminSalaryService adminSalaryService;
    private final AdminMapper adminMapper;

    @Transactional(readOnly = true)
    public SalaryInfos salaryInfos(Integer page) {
        Page<Salary> all = adminSalaryService.findAllDoneSalary(page);
        return new SalaryInfos(all.stream()
                .map(salary -> {
                    if (salary.getAccount() == null)
                        return adminMapper.mapToSalaryResponse(salary);
                    SalaryAccount account = salary.getAccount();
                    String accountNumber = encryptorUtils.decryptData(account.getAccountNumber());
                    return adminMapper.mapToSalaryResponse(salary.getSenior(), accountNumber, salary);
                })
                .toList());
    }

    public void salaryDone(Long salaryId) {
        adminSalaryService.updateDone(salaryId);
    }

    @Transactional(readOnly = true)
    public UnsettledSalaryInfos unSettledSalaryInfo(Integer page) {
        Page<Salary> salaries = adminSalaryService.findAllByNotDone(page);
        return new UnsettledSalaryInfos(salaries.stream()
                .map(salary -> {
                    SalaryAccount account = salary.getAccount();
                    if (account == null)
                        return adminMapper.mapToUnSettledSalaryResponse(salary);
                    String accountNumber = encryptorUtils.decryptData(account.getAccountNumber());
                    return adminMapper.mapToUnSettledSalaryResponse(salary, accountNumber);
                })
                .toList());
    }
}

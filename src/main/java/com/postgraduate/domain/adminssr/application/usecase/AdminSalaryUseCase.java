package com.postgraduate.domain.adminssr.application.usecase;

import com.postgraduate.domain.adminssr.application.dto.res.SalaryInfoWithOutId;
import com.postgraduate.domain.adminssr.application.dto.res.UnSettledSalaryInfo;
import com.postgraduate.domain.salary.domain.entity.Salary;
import com.postgraduate.domain.salary.domain.service.SalaryGetService;
import com.postgraduate.domain.salary.domain.service.SalaryUpdateService;
import com.postgraduate.domain.senior.domain.entity.Senior;
import com.postgraduate.domain.senior.domain.service.SeniorGetService;
import com.postgraduate.global.config.security.util.EncryptorUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static com.postgraduate.domain.adminssr.application.mapper.AdminSsrMapper.mapToSalaryResponse;
import static com.postgraduate.domain.adminssr.application.mapper.AdminSsrMapper.mapToUnSettledSalaryResponse;

@Service
@Transactional
@RequiredArgsConstructor
public class AdminSalaryUseCase {
    private final SeniorGetService seniorGetService;
    private final SalaryGetService salaryGetService;
    private final SalaryUpdateService salaryUpdateService;
    private final EncryptorUtils encryptorUtils;

    public List<SalaryInfoWithOutId> salaryInfos() {
        List<Salary> all = salaryGetService.findAll();
        return all.stream()
                .map(salary -> {
                    if (salary.getAccountNumber() == null)
                        return mapToSalaryResponse(salary.getSenior(), salary);
                    String accountNumber = encryptorUtils.decryptData(salary.getAccountNumber());
                    return mapToSalaryResponse(salary.getSenior(), accountNumber, salary);
                })
                .toList();
    }

    public SalaryInfoWithOutId seniorSalary(Long seniorId) {
        Senior senior = seniorGetService.bySeniorId(seniorId);
        Salary salary = salaryGetService.bySenior(senior);
        if (salary.getAccountNumber() == null)
            return mapToSalaryResponse(senior, salary);
        String accountNumber = encryptorUtils.decryptData(salary.getAccountNumber());
        return mapToSalaryResponse(senior, accountNumber, salary);
    }

    public void salaryDone(Long salaryId) {
        Salary salary = salaryGetService.bySalaryId(salaryId);
        salaryUpdateService.updateDone(salary);
    }

    public List<UnSettledSalaryInfo> unSettledSalaryInfo() {
        List<Salary> salaries = salaryGetService.allByNotDone();
        return salaries.stream()
                .map(salary -> {
                    if (salary.getAccountNumber() == null)
                        return mapToUnSettledSalaryResponse(salary);
                    String accountNumber = encryptorUtils.decryptData(salary.getAccountNumber());
                    return mapToUnSettledSalaryResponse(salary, accountNumber);
                })
                .toList();
    }
}

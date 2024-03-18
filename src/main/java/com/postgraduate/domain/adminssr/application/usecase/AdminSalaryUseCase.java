package com.postgraduate.domain.adminssr.application.usecase;

import com.postgraduate.domain.admin.application.dto.SalaryInfo;
import com.postgraduate.domain.admin.presentation.constant.SalaryStatus;
import com.postgraduate.domain.salary.domain.entity.Salary;
import com.postgraduate.domain.salary.domain.service.SalaryGetService;
import com.postgraduate.domain.salary.domain.service.SalaryUpdateService;
import com.postgraduate.domain.salary.exception.SalaryNotYetException;
import com.postgraduate.domain.senior.domain.entity.Senior;
import com.postgraduate.domain.senior.domain.service.SeniorGetService;
import com.postgraduate.global.config.security.util.EncryptorUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static com.postgraduate.domain.admin.application.mapper.AdminMapper.mapToSalaryResponse;
import static com.postgraduate.domain.admin.presentation.constant.SalaryStatus.DONE;
import static com.postgraduate.domain.admin.presentation.constant.SalaryStatus.YET;
import static com.postgraduate.domain.salary.util.SalaryUtil.getStatus;

@Service
@Transactional
@RequiredArgsConstructor
public class AdminSalaryUseCase {
    private final SeniorGetService seniorGetService;
    private final SalaryGetService salaryGetService;
    private final SalaryUpdateService salaryUpdateService;
    private final EncryptorUtils encryptorUtils;

    public List<SalaryInfo> salaryInfos() {
        List<Salary> all = salaryGetService.findAll();
        return all.stream()
                .filter(salary -> getStatus(salary) == DONE)
                .map(salary -> {
                    if (salary.getAccountNumber() == null)
                        return mapToSalaryResponse(salary.getSenior(), salary);
                    String accountNumber = encryptorUtils.decryptData(salary.getAccountNumber());
                    return mapToSalaryResponse(salary.getSenior(), accountNumber, salary);
                })
                .toList();
    }

    public SalaryInfo seniorSalary(Long seniorId) {
        Senior senior = seniorGetService.bySeniorId(seniorId);
        Salary salary = salaryGetService.bySeniorLastWeek(senior);
        SalaryStatus status = getStatus(salary);
        if (status != YET)
            throw new SalaryNotYetException();
        if (salary.getAccountNumber() == null)
            return mapToSalaryResponse(senior, salary);
        String accountNumber = encryptorUtils.decryptData(salary.getAccountNumber());
        return mapToSalaryResponse(senior, accountNumber, salary);
    }

    public void salaryDone(Long salaryId) {
        Salary salary = salaryGetService.bySalaryId(salaryId);
        salaryUpdateService.updateDone(salary);
    }
}

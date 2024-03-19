package com.postgraduate.domain.salary.application.usecase;

import com.postgraduate.domain.salary.application.dto.SeniorAndAccount;
import com.postgraduate.domain.salary.application.mapper.SalaryMapper;
import com.postgraduate.domain.salary.domain.entity.Salary;
import com.postgraduate.domain.salary.domain.service.SalarySaveService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;

@RequiredArgsConstructor
@Service
@Transactional
@Slf4j
public class SalaryRenewalUseCase {
    private final SalarySaveService salarySaveService;

    public void createSalaryWithAuto(SeniorAndAccount seniorAndAccount, LocalDate salaryDate) {
        Salary salary = SalaryMapper.mapToSalary(seniorAndAccount.senior(), salaryDate, seniorAndAccount.account());
        salarySaveService.save(salary);
    }
}

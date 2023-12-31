package com.postgraduate.domain.salary.application.usecase;

import com.postgraduate.domain.payment.domain.entity.Payment;
import com.postgraduate.domain.salary.application.mapper.SalaryMapper;
import com.postgraduate.domain.salary.domain.entity.Salary;
import com.postgraduate.domain.salary.domain.service.SalarySaveService;
import com.postgraduate.domain.salary.util.SalaryUtil;
import com.postgraduate.domain.senior.domain.entity.Senior;
import com.postgraduate.domain.senior.domain.service.SeniorGetService;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;

@Transactional
@Service
@RequiredArgsConstructor
public class SalaryManageUseCase {
    private final SalarySaveService salarySaveService;
    private final SeniorGetService seniorGetService;

    @Scheduled(cron = "0 0 0 10 * *", zone = "Asia/Seoul")
    public void createSalary() {
        List<Senior> seniors = seniorGetService.all();
        LocalDate salaryDate = SalaryUtil.getSalaryDate();
        seniors.forEach(senior -> {
            Salary salary = SalaryMapper.mapToSalary(senior, salaryDate);
            salarySaveService.saveSalary(salary);
        });
    }
}

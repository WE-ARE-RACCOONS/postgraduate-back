package com.postgraduate.domain.salary.application.usecase;

import com.postgraduate.domain.salary.application.dto.SeniorAndAccount;
import com.postgraduate.domain.salary.application.mapper.SalaryMapper;
import com.postgraduate.domain.salary.domain.entity.Salary;
import com.postgraduate.domain.salary.domain.service.SalaryGetService;
import com.postgraduate.domain.salary.domain.service.SalarySaveService;
import com.postgraduate.domain.senior.domain.service.SeniorGetService;
import com.postgraduate.global.slack.SlackSalaryMessage;
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
    private final SalaryGetService salaryGetService;
    private final SeniorGetService seniorGetService;
    private final SlackSalaryMessage slackSalaryMessage;

    @Scheduled(cron = "0 0 0 * * 4", zone = "Asia/Seoul")
    public void createSalary() {
        List<Salary> salaries = salaryGetService.findAllLast();
        slackSalaryMessage.sendSlackSalary(salaries);

        List<SeniorAndAccount> seniorAndAccounts = seniorGetService.findAllSeniorAndAccount();
        LocalDate salaryDate = LocalDate.now().plusDays(7);
        seniorAndAccounts.forEach(seniorAndAccount -> {
            Salary salary = SalaryMapper.mapToSalary(seniorAndAccount.senior(), salaryDate, seniorAndAccount.account());
            salarySaveService.save(salary);
        });
    }
}

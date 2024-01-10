package com.postgraduate.domain.salary.application.usecase;

import com.postgraduate.domain.account.domain.service.AccountGetService;
import com.postgraduate.domain.salary.application.dto.SeniorAndAccount;
import com.postgraduate.domain.salary.application.mapper.SalaryMapper;
import com.postgraduate.domain.salary.domain.entity.Salary;
import com.postgraduate.domain.salary.domain.service.SalaryGetService;
import com.postgraduate.domain.salary.domain.service.SalarySaveService;
import com.postgraduate.domain.salary.util.SalaryUtil;
import com.postgraduate.domain.senior.domain.service.SeniorGetService;
import com.postgraduate.global.slack.SlackMessage;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.time.LocalDate;
import java.util.List;

@Transactional
@Service
@RequiredArgsConstructor
public class SalaryManageUseCase {
    private final SalarySaveService salarySaveService;
    private final AccountGetService accountGetService;
    private final SalaryGetService salaryGetService;
    private final SeniorGetService seniorGetService;
    private final SlackMessage slackMessage;

    @Scheduled(cron = "0 0 0 10 * *", zone = "Asia/Seoul")
    public void createSalary() throws IOException {
        List<Salary> salaries = salaryGetService.findAll();
        slackMessage.sendSlackSalary(salaries);

        List<SeniorAndAccount> seniorAndAccounts = seniorGetService.findAllSeniorAndAccount();
        LocalDate salaryDate = SalaryUtil.getSalaryDate();
        seniorAndAccounts.forEach(seniorAndAccount -> {
            Salary salary = SalaryMapper.mapToSalary(seniorAndAccount.senior(), salaryDate, seniorAndAccount.account());
            salarySaveService.saveSalary(salary);
        });
    }
}

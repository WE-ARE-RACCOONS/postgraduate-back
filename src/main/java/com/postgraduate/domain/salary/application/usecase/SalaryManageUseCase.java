package com.postgraduate.domain.salary.application.usecase;

import com.postgraduate.domain.salary.application.dto.SeniorAndAccount;
import com.postgraduate.domain.salary.domain.entity.Salary;
import com.postgraduate.domain.salary.domain.service.SalaryGetService;
import com.postgraduate.domain.senior.domain.service.SeniorGetService;
import com.postgraduate.global.slack.SlackSalaryMessage;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

import static com.postgraduate.domain.salary.util.SalaryUtil.getSalaryDate;

@Service
@RequiredArgsConstructor
public class SalaryManageUseCase {
    private final SalaryGetService salaryGetService;
    private final SeniorGetService seniorGetService;
    private final SlackSalaryMessage slackSalaryMessage;
    private final SalaryRenewalUseCase salaryRenewalUseCase;

    @Scheduled(cron = "0 0 0 * * 4", zone = "Asia/Seoul")
    public void createSalary() {
        List<Salary> salaries = salaryGetService.findAllLast();
        slackSalaryMessage.sendSlackSalary(salaries);

        List<SeniorAndAccount> seniorAndAccounts = seniorGetService.findAllSeniorAndAccount();
        LocalDate salaryDate = getSalaryDate().plusDays(7);
        seniorAndAccounts.forEach(
                seniorAndAccount -> salaryRenewalUseCase.createSalaryWithAuto(seniorAndAccount, salaryDate)
        );
    }
}

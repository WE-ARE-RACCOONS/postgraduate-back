package com.postgraduate.batch.scheduler;

import com.postgraduate.domain.member.senior.domain.service.SeniorGetService;
import com.postgraduate.domain.salary.domain.service.SalaryGetService;
import com.postgraduate.global.slack.SlackErrorMessage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.JobParametersBuilder;
import org.springframework.batch.core.JobParametersInvalidException;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.batch.core.repository.JobExecutionAlreadyRunningException;
import org.springframework.batch.core.repository.JobInstanceAlreadyCompleteException;
import org.springframework.batch.core.repository.JobRestartException;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.Scheduled;

import java.time.LocalDateTime;

@Configuration
@Slf4j
@RequiredArgsConstructor
public class JobSchedulerConfig {
    private static final int MAX_RETRIES = 5;

    private final JobLauncher jobLauncher;
    @Qualifier("cancelJob")
    private final Job cancelJob;
    @Qualifier("doneJob")
    private final Job doneJob;
    @Qualifier("salaryJob")
    private final Job salaryJob;
    @Qualifier("salaryJobWithAdmin")
    private final Job salaryJobWithAdmin;

    private final SeniorGetService seniorGetService;
    private final SalaryGetService salaryGetService;
    private final SlackErrorMessage slackErrorMessage;

    @Scheduled(cron = "0 59 23 * * *", zone = "Asia/Seoul")
    public void launchCancelJob() throws JobInstanceAlreadyCompleteException, JobExecutionAlreadyRunningException, JobParametersInvalidException, JobRestartException {
        JobParameters jobParameters = new JobParametersBuilder()
                .addLocalDateTime("date", LocalDateTime.now())
                .toJobParameters();
        jobLauncher.run(cancelJob, jobParameters);
    }

    @Scheduled(cron = "0 58 23 * * *", zone = "Asia/Seoul")
    public void launchDoneJob() throws JobInstanceAlreadyCompleteException, JobExecutionAlreadyRunningException, JobParametersInvalidException, JobRestartException {
        JobParameters jobParameters = new JobParametersBuilder()
                .addLocalDateTime("date", LocalDateTime.now())
                .toJobParameters();
        jobLauncher.run(doneJob, jobParameters);
    }

    @Scheduled(cron = "0 10 0 * * 4", zone = "Asia/Seoul")
    public void launchSalaryJob() throws JobInstanceAlreadyCompleteException, JobExecutionAlreadyRunningException, JobParametersInvalidException, JobRestartException {
        JobParameters jobParameters = new JobParametersBuilder()
                .addLocalDateTime("date", LocalDateTime.now())
                .toJobParameters();
        jobLauncher.run(salaryJob, jobParameters);
        checkSalaryJobSuccess(jobParameters);
    }

    public void launchSalaryJobWithAdmin() throws JobInstanceAlreadyCompleteException, JobExecutionAlreadyRunningException, JobParametersInvalidException, JobRestartException {
        JobParameters jobParameters = new JobParametersBuilder()
                .addLocalDateTime("date", LocalDateTime.now())
                .toJobParameters();
        checkSalaryJobSuccess(jobParameters);
    }

    private void checkSalaryJobSuccess(JobParameters jobParameters) throws JobExecutionAlreadyRunningException, JobRestartException, JobInstanceAlreadyCompleteException, JobParametersInvalidException {
        int retries = 0;
        boolean success = false;
        int seniorSize = seniorGetService.allSeniorId()
                .size();
        while (retries < MAX_RETRIES){
            int salarySize = salaryGetService.findAllNext()
                    .size();
            if (salarySize == seniorSize) {
                success = true;
                break;
            }
            jobLauncher.run(salaryJobWithAdmin, jobParameters);
        }
        if (!success) {
            slackErrorMessage.sendSlackSalaryError();
        }
    }
}

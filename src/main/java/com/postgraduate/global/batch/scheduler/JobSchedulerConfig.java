package com.postgraduate.global.batch.scheduler;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.JobParametersBuilder;
import org.springframework.batch.core.configuration.JobRegistry;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.Scheduled;

import java.time.LocalDateTime;

@Configuration
@Slf4j
@RequiredArgsConstructor
public class JobSchedulerConfig {
    private final JobLauncher jobLauncher;
    @Qualifier("cancelJob")
    private final Job cancelJob;
    @Scheduled(fixedDelay = 60000)
    public void launchCancelJob() throws Exception {
        JobParameters jobParameters = new JobParametersBuilder()
                .addLocalDateTime("date", LocalDateTime.now())
                .toJobParameters();
        jobLauncher.run(cancelJob, jobParameters);
    }
}

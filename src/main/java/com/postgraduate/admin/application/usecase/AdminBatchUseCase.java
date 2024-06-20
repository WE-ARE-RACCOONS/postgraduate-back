package com.postgraduate.admin.application.usecase;

import com.postgraduate.batch.scheduler.JobSchedulerConfig;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@RequiredArgsConstructor
public class AdminBatchUseCase {
    private final JobSchedulerConfig jobSchedulerConfig;

    public void startSalaryBatch() {
        try {
            jobSchedulerConfig.launchSalaryJob();
        } catch (Exception e) {
            log.error("Create Salary Batch Exception : {}", e.getStackTrace());
        }
    }

    public void startMentoringDoneBatch() {
        try {
            jobSchedulerConfig.launchDoneJob();
        } catch (Exception e) {
            log.error("Create Salary Batch Exception : {}", e.getStackTrace());
        }
    }

    public void startMentoringCancelBatch() {
        try {
            jobSchedulerConfig.launchCancelJob();
        } catch (Exception e) {
            log.error("Create Salary Batch Exception : {}", e.getStackTrace());
        }
    }
}

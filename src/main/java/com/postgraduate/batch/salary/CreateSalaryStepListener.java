package com.postgraduate.batch.salary;

import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.*;

@Slf4j
public class CreateSalaryStepListener implements StepExecutionListener {
    @Override
    public void beforeStep(StepExecution stepExecution) {
        log.info("createSalaryStep Start");
    }

    @Override
    public ExitStatus afterStep(StepExecution stepExecution) {
        log.info("salary total read : {} and total write : {}", stepExecution.getReadCount(), stepExecution.getWriteCount());
        return stepExecution.getExitStatus();
    }
}

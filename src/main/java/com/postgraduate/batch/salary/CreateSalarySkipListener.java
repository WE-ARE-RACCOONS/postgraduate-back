package com.postgraduate.batch.salary;

import com.postgraduate.global.slack.SlackErrorMessage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.SkipListener;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
public class CreateSalarySkipListener implements SkipListener<CreateSalary, CreateSalary> {
    private final SlackErrorMessage slackErrorMessage;

    @Override
    public void onSkipInRead(Throwable t) {
        log.error("정산 자동 생성 ItemReader Skip message : {}", t.getMessage());
    }

    @Override
    public void onSkipInProcess(CreateSalary createSalary, Throwable t) {
        log.error("seniorId : {} 정산 자동 생성, message : {}", createSalary.seniorId(), t.getMessage());
        slackErrorMessage.sendSlackSalaryError(createSalary.seniorId(), t);
    }

    @Override
    public void onSkipInWrite(CreateSalary createSalary, Throwable t) {
        log.error("seniorId : {} 정산 자동 생성, message : {}", createSalary.seniorId(), t.getMessage());
        slackErrorMessage.sendSlackSalaryError(createSalary.seniorId(), t);
    }
}

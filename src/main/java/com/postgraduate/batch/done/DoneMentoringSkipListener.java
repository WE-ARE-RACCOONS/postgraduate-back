package com.postgraduate.batch.done;

import com.postgraduate.global.slack.SlackErrorMessage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.SkipListener;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
public class DoneMentoringSkipListener implements SkipListener<DoneMentoring, DoneMentoring> {
    private final SlackErrorMessage slackErrorMessage;

    @Override
    public void onSkipInRead(Throwable t) {
        log.info("멘토링 자동 완료 ItemReader Skip message : {}", t.getMessage());
    }

    @Override
    public void onSkipInProcess(DoneMentoring doneMentoring, Throwable t) {
        log.info("mentoringId : {} 자동 완료 실패, message : {}", doneMentoring.mentoringId(), t.getMessage());
        slackErrorMessage.sendSlackMentoringError(doneMentoring.mentoringId(), t);
    }

    @Override
    public void onSkipInWrite(DoneMentoring doneMentoring, Throwable t) {
        log.info("mentoringId : {} 자동 완료 실패, message : {}", doneMentoring.mentoringId(), t.getMessage());
        slackErrorMessage.sendSlackMentoringError(doneMentoring.mentoringId(), t);
    }
}

package com.postgraduate.batch.cancel;

import com.postgraduate.domain.mentoring.domain.entity.Mentoring;
import com.postgraduate.global.slack.SlackErrorMessage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.SkipListener;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
public class CancelMentoringSkipListener implements SkipListener<Mentoring, CancelMentoring> {
    private final SlackErrorMessage slackErrorMessage;

    @Override
    public void onSkipInRead(Throwable t) {
        log.error("멘토링 자동 완료 ItemReader Skip message : {}", t.getMessage());
    }

    @Override
    public void onSkipInProcess(Mentoring mentoring, Throwable t) {
        log.error("mentoringId : {} 자동 취소 실패, message : {}", mentoring.getMentoringId(), t.getMessage());
        slackErrorMessage.sendSlackMentoringError(mentoring.getMentoringId(), t);
    }

    @Override
    public void onSkipInWrite(CancelMentoring cancelMentoring, Throwable t) {
        log.error("mentoringId : {} 자동 취소 실패, message : {}", cancelMentoring.mentoringId(), t.getMessage());
        slackErrorMessage.sendSlackMentoringError(cancelMentoring.mentoringId(), t);
    }
}

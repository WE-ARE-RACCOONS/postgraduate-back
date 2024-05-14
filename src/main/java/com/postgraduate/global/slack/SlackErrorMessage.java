package com.postgraduate.global.slack;

import com.postgraduate.domain.mentoring.domain.entity.Mentoring;
import com.slack.api.Slack;
import com.slack.api.model.Attachment;
import com.slack.api.webhook.Payload;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

import static com.postgraduate.global.slack.SlackUtils.generateSlackField;

@Component
@RequiredArgsConstructor
@Slf4j
public class SlackErrorMessage {
    private final Slack slackClient = Slack.getInstance();

    @Value("${slack.log_url}")
    private String logWebHookUrl;

    public void sendSlackError(Mentoring mentoring, Exception ex) {
        try {
            slackClient.send(logWebHookUrl, Payload.builder()
                    .text("자동 갱신 에러 발생!! 백엔드팀 확인 요망!!")
                    .attachments(
                            List.of(generateErrorSlackAttachment(mentoring, ex))
                    )
                    .build());
        } catch (IOException e) {
            log.error("slack 전송 오류");
        }
    }

    public void sendSlackMentoringError(Long mentoringId, Throwable ex) {
        try {
            slackClient.send(logWebHookUrl, Payload.builder()
                    .text("멘토링 자동 갱신 에러 발생!! 백엔드팀 확인 요망!!")
                    .attachments(
                            List.of(generateMentoringErrorSlackAttachment(mentoringId, ex))
                    )
                    .build());
        } catch (IOException e) {
            log.error("slack 전송 오류");
        }
    }

    public void sendSlackSalaryError(Long seniorId, Throwable ex) {
        try {
            slackClient.send(logWebHookUrl, Payload.builder()
                    .text("정산 자동 생성 에러 발생!! 백엔드팀 확인 요망!!")
                    .attachments(
                            List.of(generateSalaryErrorSlackAttachment(seniorId, ex))
                    )
                    .build());
        } catch (IOException e) {
            log.error("slack 전송 오류");
        }
    }

    private Attachment generateMentoringErrorSlackAttachment(Long mentoringId, Throwable ex) {
        String requestTime = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:SS").format(LocalDateTime.now());
        return Attachment.builder()
                .color("ff0000")
                .title(requestTime + "에 발생한 에러 로그")
                .fields(List.of(
                        generateSlackField("Error Message", ex.getMessage()),
                        generateSlackField("Mentoring 번호", String.valueOf(mentoringId))
                ))
                .build();
    }

    private Attachment generateErrorSlackAttachment(Mentoring mentoring, Exception ex) {
        String requestTime = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:SS").format(LocalDateTime.now());
        return Attachment.builder()
                .color("ff0000")
                .title(requestTime + "에 발생한 에러 로그")
                .fields(List.of(
                        generateSlackField("Error Message", ex.getMessage()),
                        generateSlackField("Mentoring 번호", String.valueOf(mentoring.getMentoringId()))
                ))
                .build();
    }

    private Attachment generateSalaryErrorSlackAttachment(Long seniorId, Throwable ex) {
        String requestTime = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:SS").format(LocalDateTime.now());
        return Attachment.builder()
                .color("ff0000")
                .title(requestTime + "에 발생한 에러 로그")
                .fields(List.of(
                        generateSlackField("Error Message", ex.getMessage()),
                        generateSlackField("정산 자동 생성 실패, 선배 번호", String.valueOf(seniorId))
                ))
                .build();
    }
}

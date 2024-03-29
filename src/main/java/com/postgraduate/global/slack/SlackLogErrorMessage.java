package com.postgraduate.global.slack;

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

@Slf4j
@Component
@RequiredArgsConstructor
public class SlackLogErrorMessage {
    private final Slack slackClient = Slack.getInstance();

    @Value("${slack.log_url}")
    private String logWebHookUrl;

    public void sendSlackLog(Exception ex) {
        try {
            slackClient.send(logWebHookUrl, Payload.builder()
                    .text("로그 서버 에러 발생!! 백엔드팀 확인 요망!!")
                    .attachments(
                            List.of(generateLogSlackAttachment(ex))
                    )
                    .build());
        } catch (IOException e) {
            log.error("slack 전송 오류");
        }
    }

    private Attachment generateLogSlackAttachment(Exception ex) {
        String requestTime = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:SS").format(LocalDateTime.now());
        return Attachment.builder()
                .color("ff0000")
                .title(requestTime + "에 발생한 에러 로그")
                .fields(List.of(
                        generateSlackField("Error Message", ex.getMessage())
                ))
                .build();
    }
}
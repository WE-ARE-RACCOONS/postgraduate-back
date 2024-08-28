package com.postgraduate.global.slack;

import com.postgraduate.domain.senior.domain.entity.Senior;
import com.postgraduate.domain.user.domain.entity.User;
import com.slack.api.Slack;
import com.slack.api.model.Attachment;
import com.slack.api.webhook.Payload;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.List;

import static com.postgraduate.global.slack.SlackUtils.generateSlackField;

@Slf4j
@RequiredArgsConstructor
@Component
public class SlackCertificationMessage {
    private final Slack slackClient = Slack.getInstance();

    @Value("${slack.certification_url}")
    private String certificationUrl;

    public void sendCertification(Senior senior) {
        try {
            slackClient.send(certificationUrl, Payload.builder()
                    .text("선배 인증 요청이 들어왔습니다!")
                    .attachments(
                            List.of(generateCertificationAttachment(senior))
                    )
                    .build());
        } catch (IOException e) {
            log.error("slack 전송 오류");
        }
    }

    private Attachment generateCertificationAttachment(Senior senior) {
        User user = senior.getUser();
        return Attachment.builder()
                .color("2FC4B2")
                .title("선배 인증 요청")
                .fields(List.of(
                        generateSlackField("선배 닉네임 : ", user.getNickName()),
                        generateSlackField("선배 대학원 : ", senior.getInfo().getPostgradu()),
                        generateSlackField("선배 연구실 : ", senior.getInfo().getLab()),
                        generateSlackField("선배 교수 : " , senior.getInfo().getProfessor())
                ))
                .build();
    }
}

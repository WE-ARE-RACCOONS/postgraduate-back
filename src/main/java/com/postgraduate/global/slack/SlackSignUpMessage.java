package com.postgraduate.global.slack;

import com.postgraduate.domain.member.senior.domain.entity.Info;
import com.postgraduate.domain.member.senior.domain.entity.Senior;
import com.postgraduate.domain.member.user.domain.entity.User;
import com.postgraduate.domain.member.user.domain.entity.Wish;
import com.postgraduate.domain.member.user.domain.entity.constant.Status;
import com.slack.api.Slack;
import com.slack.api.model.Attachment;
import com.slack.api.webhook.Payload;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.List;

import static com.postgraduate.domain.member.user.domain.entity.constant.Status.REJECTED;
import static com.postgraduate.global.slack.SlackUtils.generateSlackField;

@Component
@RequiredArgsConstructor
@Slf4j
public class SlackSignUpMessage {
    private final Slack slackClient = Slack.getInstance();

    @Value("${slack.junior_url}")
    private String juniorUrl;
    @Value("${slack.senior_url}")
    private String seniorUrl;

    public void sendJuniorSignUp(User user, Wish wish) {
        try {
            slackClient.send(juniorUrl, Payload.builder()
                    .text("후배가 가입했습니다!")
                    .attachments(
                            List.of(generateJuniorSignUpAttachment(user, wish))
                    )
                    .build());
        } catch (IOException e) {
            log.error("slack 전송 오류");
        }
    }

    public void sendSeniorSignUp(Senior senior) {
        try {
            slackClient.send(seniorUrl, Payload.builder()
                    .text("선배가 가입했습니다!")
                    .attachments(
                            List.of(generateSeniorSignUpAttachment(senior))
                    )
                    .build());
        } catch (IOException e) {
            log.error("slack 전송 오류");
        }
    }

    private Attachment generateJuniorSignUpAttachment(User user, Wish wish) {
        LocalDateTime createdAt = user.getCreatedAt();
        Status status = wish.getStatus();
        String wantMatching = status == REJECTED ? "X" : "O";
        return Attachment.builder()
                .color("2FC4B2")
                .title("가입한 후배 정보")
                .fields(List.of(
                        generateSlackField("가입 시간 : "
                                + createdAt.getMonth().getValue() + "월 "
                                + createdAt.getDayOfMonth() + "일 "
                                + createdAt.getHour() + "시 "
                                + createdAt.getMinute() + "분 "
                                + createdAt.getSecond() + "초", null),
                        generateSlackField("후배 닉네임 : " + user.getNickName(), null),
                        generateSlackField("후배 매칭희망 여부 : " + wantMatching, null),
                        generateSlackField("후배 매칭희망 전공, 분야 : " + (wish.getMajor() + " " + wish.getField()), null)
                ))
                .build();
    }

    private Attachment generateSeniorSignUpAttachment(Senior senior) {
        LocalDateTime createdAt = senior.getCreatedAt();
        User user = senior.getUser();
        Info info = senior.getInfo();
        return Attachment.builder()
                .color("2FC4B2")
                .title("가입한 선배 정보")
                .fields(List.of(
                        generateSlackField("선배 닉네임 : " + user.getNickName(), null),
                        generateSlackField("가입 시간 : "
                                + createdAt.getMonth().getValue() + "월 "
                                + createdAt.getDayOfMonth() + "일 "
                                + createdAt.getHour() + "시 "
                                + createdAt.getMinute() + "분 "
                                + createdAt.getSecond() + "초", null),
                        generateSlackField("선배 대학원 : " + info.getPostgradu(), null),
                        generateSlackField("선배 연구실 : " + info.getLab(), null),
                        generateSlackField("선배 교수님 : " + info.getProfessor(), null)
                ))
                .build();
    }
}

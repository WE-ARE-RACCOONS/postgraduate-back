package com.postgraduate.global.slack;

import com.postgraduate.domain.senior.salary.domain.entity.Salary;
import com.slack.api.Slack;
import com.slack.api.model.Attachment;
import com.slack.api.model.Field;
import com.slack.api.webhook.Payload;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.time.LocalDate;
import java.util.List;

import static com.postgraduate.global.slack.SlackUtils.generateSlackField;

@Slf4j
@Component
@RequiredArgsConstructor
public class SlackSalaryMessage {
    private final Slack slackClient = Slack.getInstance();

    @Value("${slack.salary_url}")
    private String salaryWebHookUrl;

    public void sendSlackSalary(List<Salary> salaries) {
        List<Attachment> attachments = salaries.stream()
                .filter(salary -> salary.getTotalAmount() > 0)
                .map(this::generateSalarySlackAttachment)
                .toList();
        try {
            slackClient.send(salaryWebHookUrl, Payload.builder()
                    .text("---" + LocalDate.now() + "에 정산할 목록 ---")
                    .attachments(attachments)
                    .build());

            Attachment attachment = generateSalarySlackAttachment(salaries);
            slackClient.send(salaryWebHookUrl, Payload.builder()
                    .attachments(List.of(attachment))
                    .build());
        } catch (IOException e) {
            log.error("slack 전송 오류");
        }
    }

    //attach 생성 -> Field를 리스트로 담자
    private Attachment generateSalarySlackAttachment(Salary salary) {
        return Attachment.builder()
                .color("#36a64f")
                .fields(generateSalaryFields(salary))
                .build();
    }

    private Attachment generateSalarySlackAttachment(List<Salary> salaries) {
        return Attachment.builder()
                .color("#1900ff")
                .fields(generateTotalField(salaries))
                .build();
    }

    private List<Field> generateSalaryFields(Salary salary) {
        return List.of(
                generateSlackField(
                        "선배 닉네임 : " + salary.getSenior().getUser().getNickName(),
                        "금액 : " + salary.getTotalAmount()
                ));
    }

    private List<Field> generateTotalField(List<Salary> salaries) {
        Integer totalAmount = salaries.stream()
                .map(Salary::getTotalAmount)
                .reduce(0, Integer::sum);

        return List.of(
                generateSlackField(
                        "총 정산 금액", String.valueOf(totalAmount) + "원"
                ));
    }
}
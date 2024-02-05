package com.postgraduate.global.slack;

import com.postgraduate.domain.salary.domain.entity.Salary;
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
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Slf4j
@Component
@RequiredArgsConstructor
public class SlackMessage {
    private final Slack slackClient = Slack.getInstance();

    @Value("${slack.salary_url}")
    private String salaryWebHookUrl;
    @Value("${slack.log_url}")
    private String logWebHookUrl;

    public void sendSlackSalary(List<Salary> salaries) throws IOException{
        List<Attachment> attachments = salaries.stream()
                .filter(salary -> salary.getTotalAmount() > 0)
                .map(salary -> generateSalarySlackAttachment(salary))
                .toList();
        slackClient.send(salaryWebHookUrl, Payload.builder()
                .text("---" + LocalDate.now() + "에 정산할 목록 ---")
                .attachments(attachments)
                .build());

        Attachment attachment = generateSalarySlackAttachment(salaries);
        slackClient.send(salaryWebHookUrl, Payload.builder()
                .attachments(List.of(attachment))
                .build());
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

    public void sendSlackLog(Exception ex) throws IOException{
        slackClient.send(logWebHookUrl, Payload.builder()
                .text("로그 서버 에러 발생!! 백엔드팀 확인 요망!!")
                .attachments(
                        List.of(generateLogSlackAttachment(ex))
                )
                .build());
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

    // Field 생성 메서드
    private Field generateSlackField(String title, String value) {
        return Field.builder()
                .title(title)
                .value(value)
                .valueShortEnough(false)
                .build();
    }
}
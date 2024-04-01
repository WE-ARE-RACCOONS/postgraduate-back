package com.postgraduate.global.slack;

import com.postgraduate.domain.payment.domain.entity.Payment;
import com.postgraduate.domain.senior.domain.entity.Info;
import com.postgraduate.domain.senior.domain.entity.Senior;
import com.postgraduate.domain.user.domain.entity.User;
import com.postgraduate.domain.wish.domain.entity.Wish;
import com.postgraduate.domain.wish.domain.entity.constant.Status;
import com.slack.api.Slack;
import com.slack.api.model.Attachment;
import com.slack.api.webhook.Payload;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.List;

import static com.postgraduate.domain.wish.domain.entity.constant.Status.REJECTED;
import static com.postgraduate.global.slack.SlackUtils.generateSlackField;

@Component
@RequiredArgsConstructor
@Slf4j
public class SlackPaymentMessage {
    private final Slack slackClient = Slack.getInstance();

    @Value("${slack.pay_url}")
    private String paymentUrl;

    public void sendPayment(Payment payment) {
        try {
            slackClient.send(paymentUrl, Payload.builder()
                    .text("결제가 되었습니다!")
                    .attachments(
                            List.of(generatePaymentAttachment(payment))
                    )
                    .build());
        } catch (IOException e) {
            log.error("slack 전송 오류");
        }
    }

    //attach 생성 -> Field를 리스트로 담자
    private Attachment generatePaymentAttachment(Payment payment) {
        User user = payment.getUser();
        Senior senior = payment.getSenior();
        User seniorUser = senior.getUser();
        return Attachment.builder()
                .color("2FC4B2")
                .title("결제정보")
                .fields(List.of(
                        generateSlackField("결제 금액 : ", String.valueOf(payment.getPay())),
                        generateSlackField("후배 닉네임 : ", user.getNickName()),
                        generateSlackField("선배 닉네임 : ", seniorUser.getNickName())
                ))
                .build();
    }
}

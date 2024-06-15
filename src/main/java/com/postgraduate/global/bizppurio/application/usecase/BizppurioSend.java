package com.postgraduate.global.bizppurio.application.usecase;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.postgraduate.global.bizppurio.application.dto.req.CommonRequest;
import com.postgraduate.global.bizppurio.application.dto.res.MessageResponse;
import com.postgraduate.global.slack.SlackErrorMessage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.function.Supplier;

import static org.springframework.http.MediaType.APPLICATION_JSON;

@RequiredArgsConstructor
@Slf4j
@Component
public class BizppurioSend {
    private final BizppurioAuth bizppurioAuth;
    private final ObjectMapper objectMapper;
    private final WebClient webClient;
    private final SlackErrorMessage slackErrorMessage;

    @Value("${bizppurio.message}")
    private String messageUrl;

    protected void sendMessageWithExceptionHandling(Supplier<CommonRequest> messageSupplier) {
        try {
            CommonRequest commonRequest = messageSupplier.get();
            String accessToken = bizppurioAuth.getAuth();
            String request = objectMapper.writeValueAsString(commonRequest);
            webClient.post()
                    .uri(messageUrl)
                    .headers(h -> h.setContentType(APPLICATION_JSON))
                    .headers(h -> h.setBearerAuth(accessToken))
                    .bodyValue(request)
                    .retrieve()
                    .bodyToMono(MessageResponse.class)
                    .subscribe(this::check);
        } catch (Exception ex) {
            log.error("알림톡 전송 예외 발생: {}", ex.getMessage());
            CommonRequest commonRequest = messageSupplier.get();
            String phoneNumber = commonRequest.to();
            slackErrorMessage.sendSlackBizppurioError(phoneNumber);
        }
    }

    private void check(MessageResponse response) {
        if (response.code() != 1000) {
            log.error("전송실패 errorCode : {} errorMessage : {}", response.code(), response.description());
            throw new IllegalArgumentException(response.code() + ", " + response.description());
        }
        log.info("알림톡 전송에 성공하였습니다.");
    }
}

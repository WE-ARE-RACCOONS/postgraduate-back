package com.postgraduate.global.bizppurio.usecase;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.postgraduate.domain.senior.domain.entity.Senior;
import com.postgraduate.domain.user.domain.entity.User;
import com.postgraduate.global.bizppurio.dto.req.CommonRequest;
import com.postgraduate.global.bizppurio.dto.res.MessageResponse;
import com.postgraduate.global.bizppurio.mapper.BizppurioMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;

import static org.springframework.http.MediaType.APPLICATION_JSON;

@RequiredArgsConstructor
@Component
@Slf4j
public class BizppurioSeniorMessage {
    private final WebClient webClient;
    private final BizppurioMapper mapper;
    private final BizppurioAuth bizppurioAuth;
    private final ObjectMapper objectMapper;

    @Value("${bizppurio.message}")
    private String messageUrl;

    public void signUp(User user) {
        try {
            CommonRequest commonRequest = mapper.mapToSeniorSignUpMessage(user);
            sendMessage(commonRequest);
        } catch (Exception ex) {
            log.error("알림톡 전송 예외 발생");
            log.error("{}", ex.getMessage());
        }
    }

    public void mentoringApply(User user) {
        try {
            CommonRequest commonRequest = mapper.mapToSeniorApplyMessage(user);
            sendMessage(commonRequest);
        } catch (Exception ex) {
            log.error("알림톡 전송 예외 발생");
            log.error("{}", ex.getMessage());
        }
    }

    public void mentoringAccept(Senior senior, String time) {
        try {
            User user = senior.getUser();
            String chatLink = senior.getProfile().getChatLink();
            CommonRequest commonRequest = mapper.mapToSeniorAcceptMessage(user, chatLink, time);
            sendMessage(commonRequest);
        } catch (Exception ex) {
            log.error("알림톡 전송 예외 발생");
            log.error("{}", ex.getMessage());
        }
    }

    public void certificationApprove(User user) {
        try {
            CommonRequest commonRequest = mapper.mapToCertificationApprove(user);
            sendMessage(commonRequest);
        } catch (Exception ex) {
            log.error("알림톡 전송 예외 발생");
            log.error("{}", ex.getMessage());
        }
    }

    public void certificationDenied(User user) {
        try {
            CommonRequest commonRequest = mapper.mapToCertificationDenied(user);
            sendMessage(commonRequest);
        } catch (Exception ex) {
            log.error("알림톡 전송 예외 발생");
            log.error("{}", ex.getMessage());
        }
    }

    public void mentoringFinish(User user) {
        try {
            CommonRequest commonRequest = mapper.mapToSeniorFinish(user);
            sendMessage(commonRequest);
        } catch (Exception ex) {
            log.error("알림톡 전송 예외 발생");
            log.error("{}", ex.getMessage());
        }
    }

    private void sendMessage(CommonRequest commonRequest) throws JsonProcessingException {
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
    }

    private void check(MessageResponse response) {
        if (response.code() != 1000) {
            log.error("전송실패 errorCode : {} errorMessage : {}", response.code(), response.description());
            return;
        }
        log.info("알림톡 전송에 성공하였습니다.");
    }
}

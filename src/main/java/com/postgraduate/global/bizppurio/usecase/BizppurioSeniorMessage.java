package com.postgraduate.global.bizppurio.usecase;

import com.postgraduate.domain.user.domain.entity.User;
import com.postgraduate.global.bizppurio.dto.req.CommonRequest;
import com.postgraduate.global.bizppurio.dto.res.BizppurioTokenResponse;
import com.postgraduate.global.bizppurio.dto.res.MessageResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;

import static com.postgraduate.global.bizppurio.mapper.BizppurioMapper.mapToSeniorSignUpMessage;
import static org.springframework.http.MediaType.APPLICATION_JSON;

@RequiredArgsConstructor
@Component
@Slf4j
public class BizppurioSeniorMessage {
    private final WebClient webClient;

    @Value("${bizppurio.message}")
    private String messageUrl;
    @Value("${bizppurio.id}")
    private String bizzpurioId;

    public void signUpMessage(BizppurioTokenResponse tokenResponse, User user) {
        CommonRequest commonRequest = mapToSeniorSignUpMessage(user);
        webClient.post()
                .uri(messageUrl)
                .headers(h -> h.setContentType(APPLICATION_JSON))
                .headers(h -> h.setBearerAuth(tokenResponse.accesstoken()))
                .bodyValue(commonRequest)
                .retrieve()
                .bodyToMono(MessageResponse.class)
                .subscribe(response -> check(response));
    }

    private void check(MessageResponse response) {
        if (response.code() != 1000)
            throw new IllegalArgumentException();
        log.info("알림톡 전송에 성공하였습니다.");
    }
}

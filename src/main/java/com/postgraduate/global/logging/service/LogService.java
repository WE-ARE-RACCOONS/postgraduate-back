package com.postgraduate.global.logging.service;

import com.postgraduate.global.logging.dto.LogRequest;
import com.postgraduate.global.slack.SlackLogErrorMessage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.io.IOException;

@Service
@Slf4j
@RequiredArgsConstructor
public class LogService {
    private final WebClient webClient;
    private final SlackLogErrorMessage slackLogErrorMessage;

    @Value("${log.uri}")
    private String logUri;

    public void save(LogRequest logRequest) throws IOException {
        log.info("log save");
        webClient.post()
                .uri(logUri)
                .accept(MediaType.APPLICATION_JSON)
                .bodyValue(logRequest)
                .retrieve()
                .onStatus(HttpStatusCode::is4xxClientError, clientResponse -> {
                    log.error("클라이언트 에러 발생: " + clientResponse.statusCode());
                    return Mono.error(new RuntimeException("클라이언트 에러"));
                })
                .onStatus(HttpStatusCode::is5xxServerError, clientResponse -> {
                    log.error("서버 에러 발생: " + clientResponse.statusCode());
                    return Mono.error(new RuntimeException("서버 에러"));
                })
                .bodyToMono(Void.class)
                .doOnError(ex -> {
                    log.error("예상치 못한 에러 발생: " + ex.getMessage());
                    slackLogErrorMessage.sendSlackLog(new IllegalArgumentException("로그 서버 예외 발생"));
                })
                .subscribe();

    }
}

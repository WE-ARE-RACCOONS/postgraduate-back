package com.postgraduate.global.bizppurio.usecase;

import com.postgraduate.global.bizppurio.dto.res.BizppurioTokenResponse;
import com.postgraduate.global.config.redis.RedisRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.logging.log4j.util.Base64Util;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;

import java.time.Duration;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Optional;

import static java.time.Duration.between;
import static java.time.LocalDateTime.now;
import static java.time.LocalDateTime.parse;
import static java.time.format.DateTimeFormatter.ofPattern;
import static org.springframework.http.MediaType.APPLICATION_JSON;

@Component
@RequiredArgsConstructor
@Slf4j
public class BizppurioAuth {
    private final WebClient webClient;
    private final RedisRepository redisRepository;

    @Value("${bizppurio.token}")
    private String bizzppurioToken;
    @Value("${bizppurio.id}")
    private String bizzpurioId;
    @Value("${bizppurio.pw}")
    private String bizzpurioPw;

    public String getAuth() {
        String auth = bizzpurioId + ":" + bizzpurioPw;
        String encode = Base64Util.encode(auth);
        Optional<String> accessToken = redisRepository.getValues(encode);
        if (accessToken.isPresent()) {
            log.info("기존 알림톡 토큰 이용");
            return accessToken.get();
        }

        BizppurioTokenResponse tokenResponse = getToken(encode);
        DateTimeFormatter formatter = ofPattern("yyyyMMddHHmmss");
        LocalDateTime expiredAt = parse(tokenResponse.expired(), formatter).minusMinutes(10);
        Duration exipiredDuration = between(now(), expiredAt);
        redisRepository.setValues(encode, tokenResponse.accesstoken(), exipiredDuration);
        log.info("비즈뿌리오 토큰 {}에 만료", expiredAt);
        return tokenResponse.accesstoken();
    }

    private BizppurioTokenResponse getToken(String encode) {
        log.info("비즈뿌리오 토큰 재발급 진행");
        return webClient.post()
                .uri(bizzppurioToken)
                .headers(h -> h.setContentType(APPLICATION_JSON))
                .headers(h -> h.setBasicAuth(encode))
                .retrieve()
                .bodyToMono(BizppurioTokenResponse.class)
                .block();
    }
}

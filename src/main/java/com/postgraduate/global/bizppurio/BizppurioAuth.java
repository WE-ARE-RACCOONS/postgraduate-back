package com.postgraduate.global.bizppurio;

import com.postgraduate.domain.auth.exception.KakaoCodeException;
import com.postgraduate.global.bizppurio.dto.res.BizppurioTokenResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.Arrays;

import static java.util.Base64.getEncoder;
import static org.springframework.http.MediaType.APPLICATION_JSON;

@Component
@RequiredArgsConstructor
public class BizppurioAuth {
    private final WebClient webClient;

    @Value("${bizppurio.token}")
    private String bizzppurioToken;
    @Value("${bizppurio.id}")
    private String bizzpurioId;
    @Value("${bizppurio.pw}")
    private String bizzpurioPw;

    public BizppurioTokenResponse getAuth() {
        try {
            String auth = bizzpurioId + ":" + bizzpurioPw;
            byte[] encode = getEncoder().encode(auth.getBytes());
            return webClient.post()
                    .uri(bizzppurioToken)
                    .headers(h -> h.setContentType(APPLICATION_JSON))
                    .headers(h -> h.setBasicAuth(Arrays.toString(encode)))
                    .retrieve()
                    .bodyToMono(BizppurioTokenResponse.class)
                    .block();
        } catch (Exception ex) {
            throw new KakaoCodeException();
        }
    }
}

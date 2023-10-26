package com.postgraduate.domain.auth.application.usecase.kakao;

import com.postgraduate.domain.auth.presentation.dto.KakaoAccessTokenResponse;
import com.postgraduate.domain.auth.presentation.dto.KakaoUserInfoResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

@RequiredArgsConstructor
@Component
@Service
public class KakaoAccessTokenUseCase {

    @Value("${app-id.kakao}")
    private String APP_ID;
    private final WebClient webClient;
    private static final String USER_INFO_URI = "https://kapi.kakao.com/v2/user/me";
    private static final String VALIDATE_TOKEN_URI = "https://kapi.kakao.com/v1/user/access_token_info";


    public KakaoUserInfoResponse getUserInfo(String accessToken) {

        verifyAccessToken(accessToken);

        return webClient.get()
                .uri(USER_INFO_URI)
                .headers(h -> h.setBearerAuth(accessToken))
                .retrieve()
                .bodyToMono(KakaoUserInfoResponse.class)
                .block();
    }

    private void verifyAccessToken(String accessToken) {
        KakaoAccessTokenResponse kakaoAccessTokenResponse = webClient.get()
                .uri(VALIDATE_TOKEN_URI)
                .headers(h -> h.setBearerAuth(accessToken))
                .retrieve()
                .bodyToMono(KakaoAccessTokenResponse.class)
                .block();

        if (kakaoAccessTokenResponse == null ||
                !kakaoAccessTokenResponse.getApp_id().toString().equals(APP_ID)) {
            //TODO: 예외 처리
        }
    }
}
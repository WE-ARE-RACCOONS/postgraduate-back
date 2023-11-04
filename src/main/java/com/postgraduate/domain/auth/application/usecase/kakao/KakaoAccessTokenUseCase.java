package com.postgraduate.domain.auth.application.usecase.kakao;

import com.postgraduate.domain.auth.application.dto.res.KakaoAccessTokenResponse;
import com.postgraduate.domain.auth.application.dto.res.KakaoTokenInfoResponse;
import com.postgraduate.domain.auth.application.dto.res.KakaoUserInfoResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.reactive.function.client.WebClient;

@RequiredArgsConstructor
@Service
public class KakaoAccessTokenUseCase {

    @Value("${app-id.kakao}")
    private String APP_ID;
    @Value("${kakao.redirect-uri}")
    private String REDRECT_URI;
    @Value("${kakao.authorization-grant-type}")
    private String AUTHORIZATION_GRANT_TYPE;
    private final WebClient webClient;

    private static final String KAKAO_TOKEN_URI = "https://kauth.kakao.com/oauth/token";
    private static final String USER_INFO_URI = "https://kapi.kakao.com/v2/user/me";
    private static final String VALIDATE_TOKEN_URI = "https://kapi.kakao.com/v1/user/access_token_info";

    public KakaoUserInfoResponse getKakaoToken(String code) {
        MultiValueMap<String, String> requestBody = getRequestBody(code);
        KakaoTokenInfoResponse tokenInfoResponse = webClient.post()
                .uri(KAKAO_TOKEN_URI)
                .headers(h -> h.setContentType(MediaType.APPLICATION_FORM_URLENCODED))
                .bodyValue(requestBody)
                .retrieve()
                .bodyToMono(KakaoTokenInfoResponse.class)
                .block();
        return getUserInfo(tokenInfoResponse.getAccess_token());
    }

    private MultiValueMap<String, String> getRequestBody(String code) {
        System.out.println(code);
        MultiValueMap<String, String> requestBody = new LinkedMultiValueMap<>();
        requestBody.add("grant_type", AUTHORIZATION_GRANT_TYPE);
        requestBody.add("client_id", APP_ID);
        requestBody.add("redirect_uri", REDRECT_URI);
        requestBody.add("code", code);
        return requestBody;
    }

    private KakaoUserInfoResponse getUserInfo(String accessToken) {
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
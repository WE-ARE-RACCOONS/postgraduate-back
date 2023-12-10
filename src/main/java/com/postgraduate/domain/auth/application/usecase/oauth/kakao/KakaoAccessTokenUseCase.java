package com.postgraduate.domain.auth.application.usecase.oauth.kakao;

import com.postgraduate.domain.auth.application.dto.req.CodeRequest;
import com.postgraduate.domain.auth.application.dto.res.KakaoTokenInfoResponse;
import com.postgraduate.domain.auth.application.dto.res.KakaoUserInfoResponse;
import com.postgraduate.domain.auth.exception.KakaoCodeException;
import com.postgraduate.domain.auth.exception.KakaoException;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientResponseException;

@RequiredArgsConstructor
@Service
@Transactional
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

    public KakaoUserInfoResponse getAccessToken (CodeRequest codeRequest) {
        MultiValueMap<String, String> requestBody = getRequestBody(codeRequest.code());
        try {
            KakaoTokenInfoResponse tokenInfoResponse = webClient.post()
                    .uri(KAKAO_TOKEN_URI)
                    .headers(h -> h.setContentType(MediaType.APPLICATION_FORM_URLENCODED))
                    .bodyValue(requestBody)
                    .retrieve()
                    .bodyToMono(KakaoTokenInfoResponse.class)
                    .block();
            return getUserInfo(tokenInfoResponse.getAccess_token());
        } catch (WebClientResponseException ex) {
            throw new KakaoCodeException();
        }
    }

    private MultiValueMap<String, String> getRequestBody(String code) {
        MultiValueMap<String, String> requestBody = new LinkedMultiValueMap<>();
        requestBody.add("grant_type", AUTHORIZATION_GRANT_TYPE);
        requestBody.add("client_id", APP_ID);
        requestBody.add("redirect_uri", REDRECT_URI);
        requestBody.add("code", code);
        return requestBody;
    }

    private KakaoUserInfoResponse getUserInfo(String accessToken) {
        try {
            return webClient.get()
                    .uri(USER_INFO_URI)
                    .headers(h -> h.setBearerAuth(accessToken))
                    .retrieve()
                    .bodyToMono(KakaoUserInfoResponse.class)
                    .block();
        } catch (WebClientResponseException ex) {
            throw new KakaoException();
        }
    }
}
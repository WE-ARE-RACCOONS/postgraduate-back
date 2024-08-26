package com.postgraduate.global.auth.login.application.usecase.oauth.kakao;

import com.postgraduate.global.auth.login.application.dto.req.CodeRequest;
import com.postgraduate.global.auth.login.application.dto.res.KakaoTokenInfoResponse;
import com.postgraduate.global.auth.login.application.dto.res.KakaoUserInfoResponse;
import com.postgraduate.global.auth.login.exception.KakaoCodeException;
import com.postgraduate.global.auth.login.exception.KakaoException;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientResponseException;

@RequiredArgsConstructor
@Service
@Slf4j
public class KakaoAccessTokenUseCase {

    @Value("${app-id.kakao}")
    private String appId;
    @Value("${kakao.redirect-uri}")
    private String redirectUrl;
    @Value("${kakao.dev-redirect-uri}")
    private String devRedirectUrl;
    @Value("${kakao.authorization-grant-type}")
    private String authorizationGrantType;
    private final WebClient webClient;

    private static final String KAKAO_TOKEN_URI = "https://kauth.kakao.com/oauth/token";
    private static final String USER_INFO_URI = "https://kapi.kakao.com/v2/user/me";

    public KakaoUserInfoResponse getAccessToken (CodeRequest codeRequest) {
        MultiValueMap<String, String> requestBody = getRequestBody(codeRequest.code());
        return getKakaoUserInfoResponse(requestBody);
    }

    public KakaoUserInfoResponse getDevAccessToken (CodeRequest codeRequest) {
        MultiValueMap<String, String> requestBody = getDevRequestBody(codeRequest.code());
        return getKakaoUserInfoResponse(requestBody);
    }

    private MultiValueMap<String, String> getRequestBody(String code) {
        return getMultiValueMap(redirectUrl, code);
    }

    private MultiValueMap<String, String> getDevRequestBody(String code) {
        return getMultiValueMap(devRedirectUrl, code);
    }

    @NotNull
    private MultiValueMap<String, String> getMultiValueMap(String devRedirectUrl, String code) {
        MultiValueMap<String, String> requestBody = new LinkedMultiValueMap<>();
        requestBody.add("grant_type", authorizationGrantType);
        requestBody.add("client_id", appId);
        requestBody.add("redirect_uri", devRedirectUrl);
        requestBody.add("code", code);
        return requestBody;
    }

    private KakaoUserInfoResponse getKakaoUserInfoResponse(MultiValueMap<String, String> requestBody) {
        try {
            KakaoTokenInfoResponse tokenInfoResponse = webClient.post()
                    .uri(KAKAO_TOKEN_URI)
                    .headers(h -> h.setContentType(MediaType.APPLICATION_FORM_URLENCODED))
                    .bodyValue(requestBody)
                    .retrieve()
                    .bodyToMono(KakaoTokenInfoResponse.class)
                    .block();
            return getUserInfo(tokenInfoResponse.access_token());
        } catch (WebClientResponseException ex) {
            log.error("errorMessage : {}", ex.getMessage());
            throw new KakaoCodeException();
        }
    }

    public KakaoUserInfoResponse getUserInfo(String accessToken) {
        try {
            return webClient.get()
                    .uri(USER_INFO_URI)
                    .headers(h -> h.setBearerAuth(accessToken))
                    .retrieve()
                    .bodyToMono(KakaoUserInfoResponse.class)
                    .block();
        } catch (WebClientResponseException ex) {
            log.error("errorMessage : {}", ex.getMessage());
            throw new KakaoException();
        }
    }
}
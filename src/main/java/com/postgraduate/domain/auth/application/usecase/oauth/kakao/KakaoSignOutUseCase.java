package com.postgraduate.domain.auth.application.usecase.oauth.kakao;

import com.postgraduate.domain.auth.application.usecase.oauth.SignOutUseCase;
import com.postgraduate.domain.auth.exception.KakaoException;
import com.postgraduate.domain.user.domain.entity.User;
import com.postgraduate.domain.user.domain.service.UserGetService;
import com.postgraduate.domain.user.domain.service.UserUpdateService;
import com.postgraduate.global.config.security.jwt.util.JwtUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientResponseException;

@Service
@RequiredArgsConstructor
@Transactional
public class KakaoSignOutUseCase implements SignOutUseCase {
    private final WebClient webClient;
    private final UserUpdateService userUpdateService;
    private final UserGetService userGetService;
    private final JwtUtils jwtUtils;

    @Value("${admin-id.kakao}")
    private String ADMIN_ID;
    private static final String AUTHORIZATION = "KakaoAK ";
    private static final String KAKAO_UNLINK_URI = "https://kapi.kakao.com/v1/user/unlink";

    @Override
    public void signOut(Long userId) {
        try {
            User user = userGetService.getUser(userId);
            MultiValueMap<String, String> requestBody = getRequestBody(user.getSocialId());
            webClient.post()
                    .uri(KAKAO_UNLINK_URI)
                    .headers(h -> h.setContentType(MediaType.APPLICATION_FORM_URLENCODED))
                    .headers(h -> h.set(HttpHeaders.AUTHORIZATION, AUTHORIZATION + ADMIN_ID))
                    .bodyValue(requestBody)
                    .retrieve()
                    .bodyToMono(String.class)
                    .block();
            userUpdateService.updateDelete(user);
            jwtUtils.makeExpired(userId);
        } catch (WebClientResponseException ex) {
            throw new KakaoException();
        }
    }

    private MultiValueMap<String, String> getRequestBody(Long socialId) {
        MultiValueMap<String, String> requestBody = new LinkedMultiValueMap<>();
        requestBody.add("target_id_type", "user_id");
        requestBody.add("target_id", socialId.toString());
        return requestBody;
    }
}

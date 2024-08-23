package com.postgraduate.domain.auth.application.usecase.oauth.kakao;

import com.postgraduate.domain.auth.application.dto.req.SignOutRequest;
import com.postgraduate.domain.auth.application.usecase.oauth.SignOutUseCase;
import com.postgraduate.domain.auth.exception.KakaoException;
import com.postgraduate.domain.senior.domain.entity.Senior;
import com.postgraduate.domain.senior.domain.service.SeniorGetService;
import com.postgraduate.domain.user.quit.application.mapper.QuitMapper;
import com.postgraduate.domain.user.quit.application.utils.QuitUtils;
import com.postgraduate.domain.user.quit.domain.entity.Quit;
import com.postgraduate.domain.user.quit.domain.service.QuitSaveService;
import com.postgraduate.domain.user.user.domain.entity.User;
import com.postgraduate.domain.user.user.domain.entity.constant.Role;
import com.postgraduate.domain.user.user.domain.service.UserGetService;
import com.postgraduate.domain.user.user.domain.service.UserUpdateService;
import com.postgraduate.domain.user.user.exception.DeletedUserException;
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
    private final QuitSaveService quitSaveService;
    private final SeniorGetService seniorGetService;
    private final QuitUtils quitUtils;
    private final JwtUtils jwtUtils;

    @Value("${admin-id.kakao}")
    private String ADMIN_ID;
    private static final String AUTHORIZATION = "KakaoAK ";
    private static final String KAKAO_UNLINK_URI = "https://kapi.kakao.com/v1/user/unlink";

    @Override
    public void signOut(User user, SignOutRequest signOutRequest) {
        try {
            MultiValueMap<String, String> requestBody = getRequestBody(user.getSocialId());
            webClient.post()
                    .uri(KAKAO_UNLINK_URI)
                    .headers(h -> h.setContentType(MediaType.APPLICATION_FORM_URLENCODED))
                    .headers(h -> h.set(HttpHeaders.AUTHORIZATION, AUTHORIZATION + ADMIN_ID))
                    .bodyValue(requestBody)
                    .retrieve()
                    .bodyToMono(String.class)
                    .block();
            updateDelete(user, signOutRequest);
            jwtUtils.makeExpired(user.getUserId());
        } catch (WebClientResponseException ex) {
            throw new KakaoException();
        }
    }

    @Override
    public void reSignOut(Long socialId) {
        try {
            MultiValueMap<String, String> requestBody = getRequestBody(socialId);
            webClient.post()
                    .uri(KAKAO_UNLINK_URI)
                    .headers(h -> h.setContentType(MediaType.APPLICATION_FORM_URLENCODED))
                    .headers(h -> h.set(HttpHeaders.AUTHORIZATION, AUTHORIZATION + ADMIN_ID))
                    .bodyValue(requestBody)
                    .retrieve()
                    .bodyToMono(String.class)
                    .block();
        } catch (WebClientResponseException ex) {
            throw new KakaoException();
        }
    }

    private void updateDelete(User user, SignOutRequest signOutRequest) {
        user = userGetService.byUserId(user.getUserId());
        checkDeleteCondition(user);
        Quit quit = QuitMapper.mapToQuit(user, signOutRequest);
        quitSaveService.save(quit);
        userUpdateService.updateDelete(user);
    }

    private void checkDeleteCondition(User user) {
        if (user.isDelete())
            throw new DeletedUserException();
        if (user.getRole().equals(Role.SENIOR)) {
            Senior senior = seniorGetService.byUser(user);
            quitUtils.checkDeleteCondition(senior);
            return;
        }
        quitUtils.checkDeleteCondition(user);
    }

    private MultiValueMap<String, String> getRequestBody(Long socialId) {
        MultiValueMap<String, String> requestBody = new LinkedMultiValueMap<>();
        requestBody.add("target_id_type", "user_id");
        requestBody.add("target_id", socialId.toString());
        return requestBody;
    }
}
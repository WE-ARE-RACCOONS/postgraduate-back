package com.postgraduate.domain.auth.application.usecase.kakao;

import com.postgraduate.domain.auth.application.dto.KakaoUserInfoResponse;
import com.postgraduate.domain.user.domain.entity.User;
import com.postgraduate.domain.user.domain.service.UserSaveService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Slf4j
public class KakaoSignInUseCase {
    private final KakaoAccessTokenUseCase kakaoTokenUseCase;
    private final UserSaveService userSaveService;

    @Transactional
    public User getUser(String token) {
        KakaoUserInfoResponse userInfo = kakaoTokenUseCase.getUserInfo(token);
        Long socialId = userInfo.getId();
        return userSaveService.saveUser(socialId);
    }
}
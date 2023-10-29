package com.postgraduate.domain.auth.application.usecase.kakao;

import com.postgraduate.domain.auth.application.dto.AuthUserResponse;
import com.postgraduate.domain.auth.application.dto.KakaoUserInfoResponse;
import com.postgraduate.domain.auth.application.mapper.AuthMapper;
import com.postgraduate.domain.user.domain.entity.User;
import com.postgraduate.domain.user.domain.service.UserGetService;
import com.postgraduate.domain.user.domain.service.UserSaveService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class KakaoSignInUseCase {
    private final KakaoAccessTokenUseCase kakaoTokenUseCase;
    private final UserSaveService userSaveService;
    private final UserGetService userGetService;

    @Transactional
    public AuthUserResponse getUser(String token) {
        KakaoUserInfoResponse userInfo = kakaoTokenUseCase.getUserInfo(token);
        Long socialId = userInfo.getId();
        Optional<User> user = userGetService.bySocialId(socialId);
        if (user.isPresent()) {
            return AuthMapper.mapToAuthUser(user.get(), false);
        }

        User newUser = userSaveService.saveUser(socialId);
        return AuthMapper.mapToAuthUser(newUser, true);
    }
}
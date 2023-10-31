package com.postgraduate.domain.auth.application.usecase.kakao;

import com.postgraduate.domain.auth.application.dto.res.AuthUserResponse;
import com.postgraduate.domain.auth.application.dto.res.KakaoUserInfoResponse;
import com.postgraduate.domain.auth.application.dto.req.SignUpRequest;
import com.postgraduate.domain.auth.application.mapper.AuthMapper;
import com.postgraduate.domain.user.application.usecase.UserCheckUseCase;
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
    private final UserCheckUseCase userCheckUseCase;
    private final UserSaveService userSaveService;
    private final UserGetService userGetService;

    @Transactional
    public AuthUserResponse getUser(String token) {
        KakaoUserInfoResponse userInfo = kakaoTokenUseCase.getUserInfo(token);
        Long socialId = userInfo.getId();
        Optional<User> user = userGetService.bySocialId(socialId);
        if (user.isEmpty()) {
            return AuthMapper.mapToAuthUser(null, socialId);
        }
        return AuthMapper.mapToAuthUser(user.get(), null);
    }

    public AuthUserResponse signUp(SignUpRequest request) {
        if (userCheckUseCase.isDuplicatedNickName(request.getNickName())) {
            throw new RuntimeException("중복예외");
        }
        User user = userSaveService.saveUser(request);
        return AuthMapper.mapToAuthUser(user, null);
    }
}
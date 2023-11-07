package com.postgraduate.domain.auth.application.usecase.kakao;

import com.postgraduate.domain.auth.application.dto.req.KakaoCodeRequest;
import com.postgraduate.domain.auth.application.dto.res.AuthUserResponse;
import com.postgraduate.domain.auth.application.dto.res.KakaoUserInfoResponse;
import com.postgraduate.domain.auth.application.dto.req.SignUpRequest;
import com.postgraduate.domain.auth.application.mapper.AuthMapper;
import com.postgraduate.domain.user.application.mapper.UserMapper;
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
    public AuthUserResponse getUser(KakaoCodeRequest codeRequest) {
        KakaoUserInfoResponse userInfo = kakaoTokenUseCase.getKakaoToken(codeRequest.getCode());
        Long socialId = userInfo.getId();
        Optional<User> user = userGetService.bySocialId(socialId);
        return AuthMapper.mapToAuthUser(user, socialId);
    }

    public User signUp(SignUpRequest request) {
        User user = UserMapper.mapToUser(request);
        userSaveService.saveUser(user);
        return user;
    }
}
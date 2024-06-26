package com.postgraduate.domain.auth.application.usecase.oauth.kakao;

import com.postgraduate.domain.auth.application.dto.req.CodeRequest;
import com.postgraduate.domain.auth.application.dto.res.AuthUserResponse;
import com.postgraduate.domain.auth.application.dto.res.KakaoUserInfoResponse;
import com.postgraduate.domain.auth.application.mapper.AuthMapper;
import com.postgraduate.domain.auth.application.usecase.oauth.SignInUseCase;
import com.postgraduate.domain.user.domain.entity.User;
import com.postgraduate.domain.user.domain.service.UserGetService;
import com.postgraduate.domain.user.exception.UserNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class KakaoSignInUseCase implements SignInUseCase {
    private final KakaoAccessTokenUseCase kakaoTokenUseCase;
    private final UserGetService userGetService;

    @Override
    public AuthUserResponse getUser(CodeRequest codeRequest) {
        KakaoUserInfoResponse userInfo = kakaoTokenUseCase.getAccessToken(codeRequest);
        Long socialId = userInfo.id();
        try {
            User user = userGetService.bySocialId(socialId);
            return AuthMapper.mapToAuthUser(user, socialId);
        } catch (UserNotFoundException e) {
            return AuthMapper.mapToAuthUser(socialId);
        }
    }

    @Override
    public AuthUserResponse getDevUser(CodeRequest codeRequest) {
        KakaoUserInfoResponse userInfo = kakaoTokenUseCase.getDevAccessToken(codeRequest);
        Long socialId = userInfo.id();
        try {
            User user = userGetService.bySocialId(socialId);
            return AuthMapper.mapToAuthUser(user, socialId);
        } catch (UserNotFoundException e) {
            return AuthMapper.mapToAuthUser(socialId);
        }
    }

    /**
     * case B
     */
    @Override
    public AuthUserResponse getUserB(CodeRequest codeRequest) {
        KakaoUserInfoResponse userInfo = kakaoTokenUseCase.getAccessTokenB(codeRequest);
        Long socialId = userInfo.id();
        try {
            User user = userGetService.bySocialId(socialId);
            return AuthMapper.mapToAuthUser(user, socialId);
        } catch (UserNotFoundException e) {
            return AuthMapper.mapToAuthUser(socialId);
        }
    }
}
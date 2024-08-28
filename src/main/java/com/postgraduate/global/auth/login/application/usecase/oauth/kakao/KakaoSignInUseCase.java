package com.postgraduate.global.auth.login.application.usecase.oauth.kakao;

import com.postgraduate.global.auth.login.application.dto.req.CodeRequest;
import com.postgraduate.global.auth.login.application.dto.req.TokenRequest;
import com.postgraduate.global.auth.login.application.dto.res.AuthUserResponse;
import com.postgraduate.global.auth.login.application.dto.res.KakaoUserInfoResponse;
import com.postgraduate.global.auth.login.application.mapper.AuthMapper;
import com.postgraduate.global.auth.login.application.usecase.oauth.SignInUseCase;
import com.postgraduate.domain.user.domain.entity.User;
import com.postgraduate.domain.user.domain.service.UserGetService;
import com.postgraduate.domain.user.exception.DeletedUserException;
import com.postgraduate.domain.user.exception.UserNotFoundException;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class KakaoSignInUseCase implements SignInUseCase {
    private final KakaoAccessTokenUseCase kakaoTokenUseCase;
    private final UserGetService userGetService;
    private final KakaoSignOutUseCase kakaoSignOutUseCase;

    @Override
    public AuthUserResponse getUser(CodeRequest codeRequest) {
        KakaoUserInfoResponse userInfo = kakaoTokenUseCase.getAccessToken(codeRequest);
        return getAuthUserResponse(userInfo);
    }

    @Override
    public AuthUserResponse getDevUser(CodeRequest codeRequest) {
        KakaoUserInfoResponse userInfo = kakaoTokenUseCase.getDevAccessToken(codeRequest);
        return getAuthUserResponse(userInfo);
    }

    @Override
    public AuthUserResponse getUserWithToken(TokenRequest request) {
        KakaoUserInfoResponse userInfo = kakaoTokenUseCase.getUserInfo(request.accessToken());
        return getAuthUserResponse(userInfo);
    }

    @NotNull
    private AuthUserResponse getAuthUserResponse(KakaoUserInfoResponse userInfo) {
        Long socialId = userInfo.id();
        try {
            User user = userGetService.bySocialId(socialId);
            if (checkDelete(user))
                return AuthMapper.mapToAuthUser(socialId, true);
            return AuthMapper.mapToAuthUser(user, socialId);
        } catch (UserNotFoundException e) {
            return AuthMapper.mapToAuthUser(socialId);
        }
    }

    private boolean checkDelete(User user) {
        if (user.isDelete()) {
            if (user.isRealDelete()) {
                kakaoSignOutUseCase.reSignOut(user.getSocialId());
                throw new DeletedUserException();
            }
            return true;
        }
        return false;
    }
}
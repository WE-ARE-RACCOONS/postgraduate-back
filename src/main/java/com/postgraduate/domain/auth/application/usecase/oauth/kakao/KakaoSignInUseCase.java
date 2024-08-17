package com.postgraduate.domain.auth.application.usecase.oauth.kakao;

import com.postgraduate.domain.auth.application.dto.req.CodeRequest;
import com.postgraduate.domain.auth.application.dto.req.TokenRequest;
import com.postgraduate.domain.auth.application.dto.res.AuthUserResponse;
import com.postgraduate.domain.auth.application.dto.res.KakaoUserInfoResponse;
import com.postgraduate.domain.auth.application.mapper.AuthMapper;
import com.postgraduate.domain.auth.application.usecase.oauth.SignInUseCase;
import com.postgraduate.domain.user.user.domain.entity.User;
import com.postgraduate.domain.user.user.domain.service.UserGetService;
import com.postgraduate.domain.user.user.domain.service.UserUpdateService;
import com.postgraduate.domain.user.user.exception.DeletedUserException;
import com.postgraduate.domain.user.user.exception.UserNotFoundException;
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
    private final UserUpdateService userUpdateService;

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
            checkDelete(user);
            return AuthMapper.mapToAuthUser(user, socialId);
        } catch (UserNotFoundException e) {
            return AuthMapper.mapToAuthUser(socialId);
        }
    }

    private void checkDelete(User user) {
        if (user.isDelete()) {
            if (user.isRealDelete())
                throw new DeletedUserException(); //todo : 다시 탈퇴 처리 필요 (카카오 계정과 끊기)
            userUpdateService.updateRestore(user);
        }
    }
}
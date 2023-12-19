package com.postgraduate.domain.auth.application.usecase.oauth.kakao;

import com.postgraduate.domain.auth.application.dto.req.CodeRequest;
import com.postgraduate.domain.auth.application.dto.res.AuthUserResponse;
import com.postgraduate.domain.auth.application.dto.res.KakaoUserInfoResponse;
import com.postgraduate.domain.auth.application.mapper.AuthMapper;
import com.postgraduate.domain.auth.application.usecase.oauth.SignInUseCase;
import com.postgraduate.domain.user.domain.entity.User;
import com.postgraduate.domain.user.domain.service.UserGetService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional
public class KakaoSignInUseCase implements SignInUseCase {
    private final KakaoAccessTokenUseCase kakaoTokenUseCase;
    private final UserGetService userGetService;

    @Override
    public AuthUserResponse getUser(CodeRequest codeRequest) {
        KakaoUserInfoResponse userInfo = kakaoTokenUseCase.getAccessToken(codeRequest);
        Long socialId = userInfo.id();
        Optional<User> user = userGetService.bySocialId(socialId);
        return AuthMapper.mapToAuthUser(user, socialId);
    }
}
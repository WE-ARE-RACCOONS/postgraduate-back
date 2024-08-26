package com.postgraduate.global.auth.login.application.usecase.oauth;

import com.postgraduate.global.auth.login.application.usecase.oauth.kakao.KakaoSignInUseCase;
import com.postgraduate.global.auth.login.application.usecase.oauth.kakao.KakaoSignOutUseCase;
import com.postgraduate.global.auth.login.exception.OauthException;
import com.postgraduate.global.auth.login.presentation.constant.Provider;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@RequiredArgsConstructor
@Component
public class SelectOauth {
    private final KakaoSignInUseCase kakaoSignInUseCase;
    private final KakaoSignOutUseCase kakaoSignOutUseCase;

    public SignInUseCase selectSignIn(Provider provider) {
        if (provider.equals(Provider.KAKAO))
            return kakaoSignInUseCase;
        throw new OauthException();
    }

    public SignOutUseCase selectSignOut(Provider provider) {
        if (provider.equals(Provider.KAKAO))
            return kakaoSignOutUseCase;
        throw new OauthException();
    }
}

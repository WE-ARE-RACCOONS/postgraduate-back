package com.postgraduate.domain.auth.application.usecase.oauth;

import com.postgraduate.domain.auth.application.usecase.oauth.kakao.KakaoSignInUseCase;
import com.postgraduate.domain.auth.application.usecase.oauth.kakao.KakaoSignOutUseCase;
import com.postgraduate.domain.auth.exception.OauthException;
import com.postgraduate.domain.auth.presentation.constant.Provider;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import static com.postgraduate.domain.auth.presentation.constant.Provider.KAKAO;

@RequiredArgsConstructor
@Component
public class SelectOauth {
    private final KakaoSignInUseCase kakaoSignInUseCase;
    private final KakaoSignOutUseCase kakaoSignOutUseCase;

    public SignInUseCase selectSignIn(Provider provider) {
        if (provider.equals(KAKAO))
            return kakaoSignInUseCase;
        throw new OauthException();
    }

    public SignOutUseCase selectSignOut(Provider provider) {
        if (provider.equals(KAKAO))
            return kakaoSignOutUseCase;
        throw new OauthException();
    }
}

package com.postgraduate.domain.auth.application.usecase.oauth;

import com.postgraduate.domain.auth.application.usecase.SignInUseCase;
import com.postgraduate.domain.auth.application.usecase.oauth.kakao.KakaoSignInUseCase;
import com.postgraduate.domain.auth.exception.OauthException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@RequiredArgsConstructor
@Component
public class SelectOauth {
    private final KakaoSignInUseCase kakaoSignInUseCase;

    public SignInUseCase selectStrategy(String provider) {
        switch (provider) {
            case "KAKAO" : return kakaoSignInUseCase;
            default: throw new OauthException();
        }
    }
}

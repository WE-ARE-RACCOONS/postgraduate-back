package com.postgraduate.domain.auth.application.usecase.oauth;

import com.postgraduate.domain.auth.application.usecase.oauth.kakao.KakaoSignInUseCase;
import com.postgraduate.domain.auth.application.usecase.oauth.kakao.KakaoSignOutUseCase;
import com.postgraduate.domain.auth.exception.OauthException;
import com.postgraduate.domain.auth.presentation.constant.Provider;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import static com.postgraduate.domain.auth.presentation.constant.Provider.KAKAO;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@ExtendWith(MockitoExtension.class)
class SelectOauthTest {
    @Mock
    private KakaoSignInUseCase kakaoSignInUseCase;
    @Mock
    private KakaoSignOutUseCase kakaoSignOutUseCase;
    @InjectMocks
    private SelectOauth selectOauth;

    @Test
    @DisplayName("KAKAO 선택 테스트")
    void selectKakao() {
        Provider provider = KAKAO;

        assertThat(selectOauth.selectSignIn(provider))
                .isEqualTo(kakaoSignInUseCase);
        assertThat(selectOauth.selectSignOut(provider))
                .isEqualTo(kakaoSignOutUseCase);
    }

    @Test
    @DisplayName("없는 목록 선택 테스트")
    void selectNothing() {
        Provider mockProvider = Mockito.mock();

        assertThatThrownBy(() -> selectOauth.selectSignIn(mockProvider))
                .isInstanceOf(OauthException.class);
        assertThatThrownBy(() -> selectOauth.selectSignOut(mockProvider))
                .isInstanceOf(OauthException.class);
    }
}
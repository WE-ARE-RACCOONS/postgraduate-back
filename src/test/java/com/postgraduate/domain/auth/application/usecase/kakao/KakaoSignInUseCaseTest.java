package com.postgraduate.domain.auth.application.usecase.kakao;

import com.postgraduate.domain.auth.application.dto.req.KakaoCodeRequest;
import com.postgraduate.domain.auth.application.dto.res.AuthUserResponse;
import com.postgraduate.domain.auth.application.dto.res.KakaoUserInfoResponse;
import com.postgraduate.domain.auth.application.dto.res.KakaoUserInfoResponse.KakaoAccount;
import com.postgraduate.domain.user.domain.entity.User;
import com.postgraduate.domain.user.domain.service.UserGetService;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.mockito.BDDMockito.given;

@ExtendWith(MockitoExtension.class)
class KakaoSignInUseCaseTest {
    @Mock
    UserGetService userGetService;
    @Mock
    KakaoAccessTokenUseCase kakaoAccessTokenUseCase;
    @InjectMocks
    KakaoSignInUseCase kakaoSignInUseCase;
    @Test
    void 첫_로그인_socialId_반환() {
        String code = "abcdefg";
        KakaoCodeRequest kakaoCodeRequest = new KakaoCodeRequest(code);
        given(kakaoAccessTokenUseCase.getKakaoToken(kakaoCodeRequest)).willReturn(new KakaoUserInfoResponse(10000L, new KakaoAccount()));
        given(userGetService.bySocialId(10000L)).willReturn(Optional.ofNullable(null));
        AuthUserResponse authUserResponse = kakaoSignInUseCase.getUser(kakaoCodeRequest);
        Assertions.assertThat(authUserResponse.getSocialId()).isEqualTo(10000L);
        Assertions.assertThat(authUserResponse.getUser().isEmpty()).isTrue();
    }

    @Test
    void 기존_유저_로그인_user반환() {
        String code = "abcdefg";
        KakaoCodeRequest kakaoCodeRequest = new KakaoCodeRequest(code);
        given(kakaoAccessTokenUseCase.getKakaoToken(kakaoCodeRequest)).willReturn(new KakaoUserInfoResponse(10000L, new KakaoAccount()));
        given(userGetService.bySocialId(10000L)).willReturn(Optional.ofNullable(new User()));
        AuthUserResponse authUserResponse = kakaoSignInUseCase.getUser(kakaoCodeRequest);
        Assertions.assertThat(authUserResponse.getSocialId()).isEqualTo(10000L);
        Assertions.assertThat(authUserResponse.getUser().isEmpty()).isFalse();
    }
}
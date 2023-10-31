package com.postgraduate.domain.auth.application.usecase.kakao;

import com.postgraduate.domain.auth.application.dto.req.SignUpRequest;
import com.postgraduate.domain.user.application.usecase.UserCheckUseCase;
import com.postgraduate.domain.user.domain.service.UserGetService;
import com.postgraduate.domain.user.domain.service.UserSaveService;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class KakaoSignInUseCaseTest {

    @Test
    void 중복_아이디를_입력하면_예외를_발생시킨다() {
        KakaoAccessTokenUseCase kakaoAccessTokenUseCase = mock();
        UserCheckUseCase userCheckUseCase = mock();
        UserSaveService userSaveService = mock();
        UserGetService userGetService = mock();

        SignUpRequest request = new SignUpRequest(23423423L, "nickname");

        when(userCheckUseCase.isDuplicatedNickName(request.getNickName())).thenReturn(true);

        KakaoSignInUseCase kakaoSignInUseCase = new KakaoSignInUseCase(kakaoAccessTokenUseCase, userCheckUseCase, userSaveService, userGetService);

        assertThrows(RuntimeException.class, () -> kakaoSignInUseCase.signUp(request));
    }
}
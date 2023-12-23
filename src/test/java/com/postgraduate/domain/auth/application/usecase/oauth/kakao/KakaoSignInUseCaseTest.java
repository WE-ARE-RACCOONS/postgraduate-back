package com.postgraduate.domain.auth.application.usecase.oauth.kakao;

import com.postgraduate.domain.auth.application.dto.req.CodeRequest;
import com.postgraduate.domain.auth.application.dto.res.AuthUserResponse;
import com.postgraduate.domain.auth.application.dto.res.KakaoUserInfoResponse;
import com.postgraduate.domain.user.domain.entity.User;
import com.postgraduate.domain.user.domain.service.UserGetService;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.Optional;

import static com.postgraduate.domain.auth.application.dto.res.KakaoUserInfoResponse.KakaoAccount;
import static com.postgraduate.domain.user.domain.entity.constant.Role.USER;
import static java.lang.Boolean.TRUE;
import static java.time.LocalDate.now;
import static org.mockito.BDDMockito.given;

@ExtendWith(MockitoExtension.class)
class KakaoSignInUseCaseTest {
    @Mock
    private KakaoAccessTokenUseCase kakaoAccessTokenUseCase;
    @Mock
    private UserGetService userGetService;
    @InjectMocks
    private KakaoSignInUseCase kakaoSignInUseCase;

    private User user;

    @BeforeEach
    void setting() {
        user = new User(1L, 1L, "a",
                "a", "123", "a",
                1, USER, TRUE, LocalDateTime.now(), LocalDateTime.now(), TRUE);
    }
    @Test
    @DisplayName("기존 회원 테스트")
    void getUserWithUser() {
        CodeRequest codeRequest = new CodeRequest("code");
        KakaoAccount  kakaoAccount = new KakaoAccount("email");
        KakaoUserInfoResponse kakaoUserInfoResponse = new KakaoUserInfoResponse(1L, kakaoAccount);

        given(kakaoAccessTokenUseCase.getAccessToken(codeRequest))
                .willReturn(kakaoUserInfoResponse);
        given(userGetService.bySocialId(kakaoUserInfoResponse.id()))
                .willReturn(Optional.of(user));

        AuthUserResponse authUserResponse = kakaoSignInUseCase.getUser(codeRequest);

        Assertions.assertThat(authUserResponse.getUser())
                .isNotEmpty();
    }

    @Test
    @DisplayName("새로운 회원 테스트")
    void getUserWithNull() {
        CodeRequest codeRequest = new CodeRequest("code");
        KakaoAccount  kakaoAccount = new KakaoAccount("email");
        KakaoUserInfoResponse kakaoUserInfoResponse = new KakaoUserInfoResponse(100000L, kakaoAccount);

        given(kakaoAccessTokenUseCase.getAccessToken(codeRequest))
                .willReturn(kakaoUserInfoResponse);
        given(userGetService.bySocialId(kakaoUserInfoResponse.id()))
                .willReturn(Optional.ofNullable(null));

        AuthUserResponse authUserResponse = kakaoSignInUseCase.getUser(codeRequest);

        Assertions.assertThat(authUserResponse.getUser())
                .isEmpty();
    }
}
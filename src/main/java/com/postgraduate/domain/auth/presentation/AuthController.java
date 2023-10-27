package com.postgraduate.domain.auth.presentation;

import com.postgraduate.domain.auth.application.usecase.kakao.KakaoSignInUseCase;
import com.postgraduate.domain.auth.application.dto.JwtTokenResponse;
import com.postgraduate.domain.auth.application.dto.KakaoLoginRequest;
import com.postgraduate.domain.user.domain.entity.User;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@Slf4j
public class AuthController {
    private final KakaoSignInUseCase kakaoSignInUseCase;

    @PostMapping("/login")
    public void getUserDetails(@RequestBody KakaoLoginRequest request) {
        User user = kakaoSignInUseCase.getUser(request.getAccessToken());
        //TODO: JWT 발급 + 신규 여부
    }
}

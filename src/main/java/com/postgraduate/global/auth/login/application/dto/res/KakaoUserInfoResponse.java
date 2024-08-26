package com.postgraduate.global.auth.login.application.dto.res;

public record KakaoUserInfoResponse(Long id, KakaoAccount kakaoAccount) {
    public record KakaoAccount(String email) {}
}

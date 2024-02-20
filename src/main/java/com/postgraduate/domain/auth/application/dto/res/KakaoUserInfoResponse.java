package com.postgraduate.domain.auth.application.dto.res;

public record KakaoUserInfoResponse(Long id, KakaoAccount kakaoAccount) {
    public record KakaoAccount(String email) {}
}

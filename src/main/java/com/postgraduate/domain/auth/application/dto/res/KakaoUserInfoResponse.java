package com.postgraduate.domain.auth.application.dto.res;

import lombok.*;

public record KakaoUserInfoResponse(Long id, KakaoAccount kakaoAccount) {
    public record KakaoAccount(String email) {}
}

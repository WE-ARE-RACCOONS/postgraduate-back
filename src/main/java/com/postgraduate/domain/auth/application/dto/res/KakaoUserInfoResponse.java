package com.postgraduate.domain.auth.application.dto.res;

import lombok.*;

@Builder
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class KakaoUserInfoResponse {
    private Long id;
    private KakaoAccount kakaoAccount;

    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class KakaoAccount {
        private String email;
    }
}

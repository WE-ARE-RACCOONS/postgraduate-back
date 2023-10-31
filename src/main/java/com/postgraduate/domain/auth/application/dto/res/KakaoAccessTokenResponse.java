package com.postgraduate.domain.auth.application.dto.res;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Builder
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class KakaoAccessTokenResponse {
    Integer app_id;
}

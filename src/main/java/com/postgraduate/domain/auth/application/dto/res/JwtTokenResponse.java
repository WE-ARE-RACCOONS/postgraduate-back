package com.postgraduate.domain.auth.application.dto.res;

import com.postgraduate.domain.user.domain.entity.constant.Role;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Builder
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class JwtTokenResponse {
    private String accessToken;
    private String refreshToken;
    private Role role;
}

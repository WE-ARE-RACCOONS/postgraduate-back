package com.postgraduate.domain.auth.application.dto.res;

import com.postgraduate.domain.user.domain.entity.constant.Role;
import lombok.*;

@Builder
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class JwtTokenResponse {
    private String accessToken;
    private int accessExpiration;
    private String refreshToken;
    private int refreshExpiration;
    private Role role;
}

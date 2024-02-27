package com.postgraduate.domain.auth.application.dto.res;

import com.postgraduate.domain.user.domain.entity.constant.Role;

public record JwtTokenResponse(String accessToken, int accessExpiration,
                               String refreshToken, int refreshExpiration, Role role) implements AuthResponse{}

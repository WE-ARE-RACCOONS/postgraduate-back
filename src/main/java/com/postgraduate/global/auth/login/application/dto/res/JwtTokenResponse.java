package com.postgraduate.global.auth.login.application.dto.res;

import com.postgraduate.domain.member.user.domain.entity.constant.Role;

public record JwtTokenResponse(String accessToken, int accessExpiration,
                               String refreshToken, int refreshExpiration, Role role, boolean isTutorial) implements AuthResponse{}

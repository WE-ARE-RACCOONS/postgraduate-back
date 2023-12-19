package com.postgraduate.domain.auth.application.dto.res;

import com.postgraduate.domain.user.domain.entity.constant.Role;
import jakarta.validation.constraints.NotNull;

public record JwtTokenResponse(@NotNull String accessToken, @NotNull int accessExpiration,
                               @NotNull String refreshToken, int refreshExpiration, Role role) {}

package com.postgraduate.domain.auth.application.dto.res;

import jakarta.validation.constraints.NotNull;

public record KakaoTokenInfoResponse(@NotNull String access_token, @NotNull String token_type, @NotNull String refresh_token, @NotNull String id_token,
                                     @NotNull int expires_in, @NotNull String cope, @NotNull int refresh_token_expires_in) {
}

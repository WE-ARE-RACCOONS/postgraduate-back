package com.postgraduate.domain.user.application.dto.res;

import jakarta.validation.constraints.NotNull;

public record UserInfoResponse(
        @NotNull
        String profile,
        @NotNull
        String nickName,
        @NotNull
        String phoneNumber
) { }

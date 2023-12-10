package com.postgraduate.domain.user.application.dto.res;

import jakarta.validation.constraints.NotNull;

public record UserMyPageResponse(
        @NotNull
        String nickName,
        @NotNull
        String profile
) { }

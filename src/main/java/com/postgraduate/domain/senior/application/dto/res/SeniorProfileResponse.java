package com.postgraduate.domain.senior.application.dto.res;

import jakarta.validation.constraints.NotNull;

public record SeniorProfileResponse(
        @NotNull
        String nickName,
        @NotNull
        String profile,
        @NotNull
        String postgradu,
        @NotNull
        String major,
        @NotNull
        String lab,
        @NotNull
        Integer term
) {
}

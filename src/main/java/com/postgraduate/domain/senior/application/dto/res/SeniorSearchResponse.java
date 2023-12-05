package com.postgraduate.domain.senior.application.dto.res;

import jakarta.validation.constraints.NotNull;

public record SeniorSearchResponse(
        @NotNull
        Long seniorId,
        @NotNull
        String profile,
        @NotNull
        String nickName,
        @NotNull
        String postgradu,
        @NotNull
        String major,
        @NotNull
        String lab,
        @NotNull
        String oneLiner,
        @NotNull
        String[] keyword
) {}

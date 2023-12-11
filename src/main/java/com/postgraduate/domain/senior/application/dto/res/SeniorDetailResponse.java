package com.postgraduate.domain.senior.application.dto.res;

import jakarta.validation.constraints.NotNull;

public record SeniorDetailResponse(
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
        String professor,
        @NotNull
        String[] keyword,
        @NotNull
        String info,
        @NotNull
        String oneLiner,
        @NotNull
        String target,
        @NotNull
        String time
) { }

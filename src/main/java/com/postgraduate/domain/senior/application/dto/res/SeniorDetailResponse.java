package com.postgraduate.domain.senior.application.dto.res;

import com.postgraduate.domain.available.application.dto.res.AvailableTimeResponse;
import jakarta.validation.constraints.NotNull;

import java.util.List;

public record SeniorDetailResponse(
        @NotNull
        String nickName,
        @NotNull
        int term,
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
        List<AvailableTimeResponse> times
) { }

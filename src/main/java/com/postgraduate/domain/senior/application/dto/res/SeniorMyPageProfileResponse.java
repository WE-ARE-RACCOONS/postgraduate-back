package com.postgraduate.domain.senior.application.dto.res;


import com.postgraduate.domain.available.application.dto.res.AvailableTimeResponse;
import jakarta.validation.constraints.NotNull;

import java.util.List;

public record SeniorMyPageProfileResponse(
        @NotNull
        String lab,
        @NotNull
        String[] keyword,
        @NotNull
        String info,
        @NotNull
        String target,
        @NotNull
        String chatLink,
        @NotNull
        String[] field,
        @NotNull
        String oneLiner,
        @NotNull
        List<AvailableTimeResponse> times
) { }

package com.postgraduate.domain.senior.application.dto.req;

import com.postgraduate.domain.available.application.dto.req.AvailableCreateRequest;
import jakarta.validation.constraints.NotNull;

import java.util.List;

public record SeniorProfileRequest(
        @NotNull
        String info,
        @NotNull
        String target,
        @NotNull
        String chatLink,
        @NotNull
        String oneLiner,
        @NotNull
        List<AvailableCreateRequest> times
) {}

package com.postgraduate.domain.member.senior.application.dto.req;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;

import java.util.List;

public record SeniorProfileRequest(
        @NotBlank
        String info,
        @NotBlank
        String target,
        @NotBlank
        String oneLiner,
        @NotEmpty
        List<AvailableCreateRequest> times
) {}

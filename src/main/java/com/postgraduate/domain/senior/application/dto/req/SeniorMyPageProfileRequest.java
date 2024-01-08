package com.postgraduate.domain.senior.application.dto.req;

import com.postgraduate.domain.available.application.dto.req.AvailableCreateRequest;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;

import java.util.List;

public record SeniorMyPageProfileRequest(
        @NotBlank
        String lab,
        @NotBlank
        String keyword,
        @NotBlank
        String info,
        @NotBlank
        String target,
        @NotBlank
        String chatLink,
        @NotBlank
        String field,
        @NotBlank
        String oneLiner,
        @NotEmpty
        List<AvailableCreateRequest> times
) {}

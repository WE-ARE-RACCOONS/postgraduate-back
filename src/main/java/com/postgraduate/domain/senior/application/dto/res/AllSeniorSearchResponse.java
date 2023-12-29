package com.postgraduate.domain.senior.application.dto.res;

import jakarta.validation.constraints.NotNull;

import java.util.List;

public record AllSeniorSearchResponse(
        @NotNull
        List<SeniorSearchResponse> seniorSearchResponses,
        @NotNull
        Long totalElements
) {
}

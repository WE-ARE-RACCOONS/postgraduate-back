package com.postgraduate.domain.senior.application.dto.req;

import jakarta.validation.constraints.NotNull;

public record SeniorProfileRequest(
        @NotNull
        String info,
        @NotNull
        String target,
        @NotNull
        String chatLink,
        @NotNull
        String time,
        @NotNull
        String oneLiner
) {
}

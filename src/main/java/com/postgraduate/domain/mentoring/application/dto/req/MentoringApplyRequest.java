package com.postgraduate.domain.mentoring.application.dto.req;

import jakarta.validation.constraints.NotNull;

public record MentoringApplyRequest(
        @NotNull
        Long seniorId,
        @NotNull
        String topic,
        @NotNull
        String question,
        @NotNull
        String date
) { }

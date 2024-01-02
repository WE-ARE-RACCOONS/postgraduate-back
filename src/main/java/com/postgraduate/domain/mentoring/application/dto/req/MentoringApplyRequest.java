package com.postgraduate.domain.mentoring.application.dto.req;

import jakarta.validation.constraints.NotBlank;

public record MentoringApplyRequest(
        Long seniorId,
        @NotBlank
        String topic,
        @NotBlank
        String question,
        @NotBlank
        String date
) { }

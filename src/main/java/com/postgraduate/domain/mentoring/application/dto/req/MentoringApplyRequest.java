package com.postgraduate.domain.mentoring.application.dto.req;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record MentoringApplyRequest(
        @NotNull
        Long seniorId,
        @NotBlank
        String topic,
        @NotBlank
        String question,
        @NotBlank
        String date
) {
}

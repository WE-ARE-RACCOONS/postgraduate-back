package com.postgraduate.domain.mentoring.application.dto.req;

import jakarta.validation.constraints.NotBlank;

public record MentoringDateRequest(@NotBlank String date) {
}

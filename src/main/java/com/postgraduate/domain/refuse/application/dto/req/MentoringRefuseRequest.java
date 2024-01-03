package com.postgraduate.domain.refuse.application.dto.req;

import jakarta.validation.constraints.NotBlank;

public record MentoringRefuseRequest(@NotBlank String reason) {
}

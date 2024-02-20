package com.postgraduate.domain.auth.application.dto.req;

import jakarta.validation.constraints.NotBlank;

public record CodeRequest(@NotBlank String code) {
}

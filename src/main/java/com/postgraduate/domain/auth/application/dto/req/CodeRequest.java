package com.postgraduate.domain.auth.application.dto.req;

import jakarta.validation.constraints.NotNull;

public record CodeRequest(@NotNull String code) {
}

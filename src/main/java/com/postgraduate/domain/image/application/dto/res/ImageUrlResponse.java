package com.postgraduate.domain.image.application.dto.res;

import jakarta.validation.constraints.NotNull;

public record ImageUrlResponse(@NotNull String profileUrl) {}

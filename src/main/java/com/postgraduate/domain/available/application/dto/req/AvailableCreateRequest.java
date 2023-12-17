package com.postgraduate.domain.available.application.dto.req;

import jakarta.validation.constraints.NotNull;

public record AvailableCreateRequest(@NotNull String day, @NotNull String startTime, @NotNull String endTime) {}

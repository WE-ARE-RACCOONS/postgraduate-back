package com.postgraduate.domain.senior.application.dto.req;

import jakarta.validation.constraints.NotBlank;

public record AvailableCreateRequest(@NotBlank String day, @NotBlank String startTime, @NotBlank String endTime) {}

package com.postgraduate.domain.available.application.dto.res;

import jakarta.validation.constraints.NotNull;

public record AvailableTimeResponse (@NotNull String day, @NotNull String startTime, @NotNull String endTime){}

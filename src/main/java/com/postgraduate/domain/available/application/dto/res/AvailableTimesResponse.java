package com.postgraduate.domain.available.application.dto.res;

import jakarta.validation.constraints.NotNull;

import java.util.List;

public record AvailableTimesResponse (@NotNull List<AvailableTimeResponse> times){}

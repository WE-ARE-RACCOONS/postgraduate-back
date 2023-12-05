package com.postgraduate.domain.senior.application.dto.res;

import jakarta.validation.constraints.NotNull;

import java.util.List;

public record AllSeniorFieldResponse(@NotNull List<SeniorFieldResponse> seniorFieldResponses) { }

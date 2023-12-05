package com.postgraduate.domain.admin.application.dto.res;

import jakarta.validation.constraints.NotNull;

import java.util.List;

public record AllSalariesResponse(@NotNull List<SalariesResponse> salariesResponses) {
}

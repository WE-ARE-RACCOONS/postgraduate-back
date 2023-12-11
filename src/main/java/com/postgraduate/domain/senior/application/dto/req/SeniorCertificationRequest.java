package com.postgraduate.domain.senior.application.dto.req;

import jakarta.validation.constraints.NotNull;

public record SeniorCertificationRequest(
        @NotNull
        String certification
) { }

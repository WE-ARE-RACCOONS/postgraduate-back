package com.postgraduate.domain.senior.application.dto.req;

import jakarta.validation.constraints.NotNull;

public record SeniorAccountRequest(
        @NotNull
        String accountNumber,
        @NotNull
        String bank,
        @NotNull
        String accountHolder
) { }

package com.postgraduate.domain.senior.application.dto.req;

import jakarta.validation.constraints.NotBlank;

public record SeniorAccountRequest(
        @NotBlank
        String accountNumber,
        @NotBlank
        String bank,
        @NotBlank
        String accountHolder
) { }

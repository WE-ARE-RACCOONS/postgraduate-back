package com.postgraduate.domain.senior.application.dto.req;

import jakarta.validation.constraints.NotNull;

public record SeniorMyPageUserAccountRequest(
        @NotNull String nickName,
        @NotNull String accountNumber,
        @NotNull String bank,
        @NotNull String accountHolder,
        @NotNull String profile
) {}

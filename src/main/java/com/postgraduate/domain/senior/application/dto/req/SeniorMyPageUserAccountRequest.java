package com.postgraduate.domain.senior.application.dto.req;

import jakarta.validation.constraints.NotNull;

public record SeniorMyPageUserAccountRequest(
        @NotNull String nickName,
        @NotNull String phoneNumber,
        @NotNull String profile,
        String accountNumber,
        String bank,
        String accountHolder
) {}

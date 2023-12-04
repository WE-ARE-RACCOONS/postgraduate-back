package com.postgraduate.domain.admin.application.dto.res;

import jakarta.annotation.Nullable;

public record SalaryResponse(
        @Nullable String nickName,
        @Nullable String phoneNumber,
        @Nullable Integer totalAmount,
        @Nullable String accountHolder,
        @Nullable String bank,
        @Nullable String accountNumber,
        @Nullable Boolean status
) {}

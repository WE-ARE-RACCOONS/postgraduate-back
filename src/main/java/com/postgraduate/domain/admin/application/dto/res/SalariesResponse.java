package com.postgraduate.domain.admin.application.dto.res;

import jakarta.validation.constraints.NotNull;

import java.time.LocalDateTime;

public record SalariesResponse(
        @NotNull
        String nickName,
        @NotNull
        String phoneNumber,
        @NotNull
        Integer totalAmount,
        @NotNull
        String accountHolder,
        @NotNull
        String bank,
        @NotNull
        String accountNumber,
        @NotNull
        LocalDateTime salaryDoneDate
) {}

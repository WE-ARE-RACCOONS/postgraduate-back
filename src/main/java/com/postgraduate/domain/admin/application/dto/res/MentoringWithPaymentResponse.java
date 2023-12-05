package com.postgraduate.domain.admin.application.dto.res;

import jakarta.validation.constraints.NotNull;

public record MentoringWithPaymentResponse(
        @NotNull
        String userNickname,
        @NotNull
        String userPhoneNumber,
        @NotNull
        String seniorNickname,
        @NotNull
        String seniorPhoneNumber,
        @NotNull
        String date,
        @NotNull
        int term,
        int pay,
        int charge
) { }

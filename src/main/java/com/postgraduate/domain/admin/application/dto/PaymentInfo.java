package com.postgraduate.domain.admin.application.dto;

import jakarta.validation.constraints.NotNull;

import java.time.LocalDate;

public record PaymentInfo(
        @NotNull
        Long paymentId,
        @NotNull
        Long mentoringId,
        @NotNull
        String userNickName,
        @NotNull
        String seniorNickName,
        @NotNull
        LocalDate createdAt,
        @NotNull
        Integer pay
) { }

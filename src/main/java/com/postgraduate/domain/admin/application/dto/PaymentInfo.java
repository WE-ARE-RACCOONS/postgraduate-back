package com.postgraduate.domain.admin.application.dto;

import com.postgraduate.domain.payment.domain.entity.constant.Status;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDateTime;

public record PaymentInfo(
        @NotNull
        Long paymentId,
        @NotNull
        Long mentoringId,
        @NotNull
        String userNickName, //todo: payple 실명
        @NotNull
        String phoneNumber,
        @NotNull
        LocalDateTime createdAt,
        @NotNull
        Integer pay,
        @NotNull
        Status status
) { }

package com.postgraduate.admin.application.dto.res;

import com.postgraduate.domain.payment.domain.entity.constant.Status;

import java.time.LocalDateTime;

public record PaymentInfo(
        Long paymentId,
        Long mentoringId,
        String userNickName,
        String phoneNumber,
        LocalDateTime createdAt,
        Integer pay,
        Status status
) { }

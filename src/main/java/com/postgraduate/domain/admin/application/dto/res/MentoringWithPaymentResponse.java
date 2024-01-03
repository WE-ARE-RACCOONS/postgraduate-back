package com.postgraduate.domain.admin.application.dto.res;

public record MentoringWithPaymentResponse(
        Long paymentId,
        String userNickname,
        String userPhoneNumber,
        String seniorNickname,
        String seniorPhoneNumber,
        String date,
        int term,
        int pay,
        int charge
) { }

package com.postgraduate.domain.adminssr.application.dto.res;

public record MentoringWithPaymentResponse(
        Long mentoringId,
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

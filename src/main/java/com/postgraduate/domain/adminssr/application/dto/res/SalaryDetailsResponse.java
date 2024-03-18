package com.postgraduate.domain.adminssr.application.dto.res;

public record SalaryDetailsResponse(
        String nickName,
        String phoneNumber,
        Integer totalAmount,
        String accountHolder,
        String bank,
        String accountNumber,
        Boolean status
) {}

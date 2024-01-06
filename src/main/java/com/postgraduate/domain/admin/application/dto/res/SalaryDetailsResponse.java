package com.postgraduate.domain.admin.application.dto.res;

import com.postgraduate.domain.admin.presentation.constant.SalaryStatus;

public record SalaryDetailsResponse(
        String nickName,
        String phoneNumber,
        Integer totalAmount,
        String accountHolder,
        String bank,
        String accountNumber,
        SalaryStatus status
) {}

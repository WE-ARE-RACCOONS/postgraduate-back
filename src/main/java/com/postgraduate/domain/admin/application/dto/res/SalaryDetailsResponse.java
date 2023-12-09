package com.postgraduate.domain.admin.application.dto.res;

import com.postgraduate.domain.admin.presentation.constant.SalaryStatus;
import jakarta.validation.constraints.NotNull;

public record SalaryDetailsResponse(
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
        SalaryStatus status
) {}

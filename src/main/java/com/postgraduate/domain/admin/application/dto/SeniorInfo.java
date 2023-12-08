package com.postgraduate.domain.admin.application.dto;

import com.postgraduate.domain.salary.domain.entity.constant.SalaryStatus;
import com.postgraduate.domain.senior.domain.entity.constant.Status;
import jakarta.validation.constraints.NotNull;

public record SeniorInfo(
        @NotNull
        Long seniorId,
        @NotNull
        String nickName,
        @NotNull
        String phoneNumber,
        @NotNull
        Status status,
        @NotNull
        SalaryStatus salaryStatus,
        @NotNull
        Boolean marketingReceive,
        @NotNull
        Boolean isUser
) {
}

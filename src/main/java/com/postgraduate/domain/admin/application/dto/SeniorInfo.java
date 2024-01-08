package com.postgraduate.domain.admin.application.dto;

import com.postgraduate.domain.admin.presentation.constant.SalaryStatus;
import com.postgraduate.domain.senior.domain.entity.constant.Status;

public record SeniorInfo(
        Long seniorId,
        String nickName,
        String phoneNumber,
        Status certificationStatus,
        SalaryStatus salaryStatus,
        Boolean marketingReceive,
        Boolean isUser
) {
}

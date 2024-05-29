package com.postgraduate.admin.application.dto.res;

import com.postgraduate.domain.senior.domain.entity.constant.Status;

public record SeniorInfo(
        Long seniorId,
        String nickName,
        String phoneNumber,
        Status certificationStatus,
        int totalAmount,
        Boolean marketingReceive,
        Boolean isUser
) {
}

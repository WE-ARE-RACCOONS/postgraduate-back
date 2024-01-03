package com.postgraduate.domain.admin.application.dto.res;

import com.postgraduate.domain.admin.application.dto.PaymentInfo;

import java.util.List;

public record PaymentManageResponse(
        List<PaymentInfo> paymentInfo,
        Long totalElements,
        Integer totalPages
) {
}

package com.postgraduate.domain.admin.application.dto.res;

import com.postgraduate.domain.admin.application.dto.PaymentInfo;
import jakarta.validation.constraints.NotNull;

import java.util.List;

public record PaymentManageResponse(@NotNull List<PaymentInfo> paymentInfo) {
}

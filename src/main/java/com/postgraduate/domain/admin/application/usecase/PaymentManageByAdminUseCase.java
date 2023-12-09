package com.postgraduate.domain.admin.application.usecase;

import com.postgraduate.domain.admin.application.dto.PaymentInfo;
import com.postgraduate.domain.admin.application.dto.res.PaymentManageResponse;
import com.postgraduate.domain.admin.application.mapper.AdminMapper;
import com.postgraduate.domain.payment.domain.entity.Payment;
import com.postgraduate.domain.payment.domain.service.PaymentGetService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
public class PaymentManageByAdminUseCase {
    private final PaymentGetService paymentGetService;
    public PaymentManageResponse getPayments() {
        List<Payment> payments = paymentGetService.all();
        List<PaymentInfo> paymentInfos = payments.stream()
                .map(AdminMapper::mapToPaymentInfo)
                .toList();
        return new PaymentManageResponse(paymentInfos);
    }
}

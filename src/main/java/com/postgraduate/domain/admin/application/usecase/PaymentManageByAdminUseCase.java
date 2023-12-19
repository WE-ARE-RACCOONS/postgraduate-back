package com.postgraduate.domain.admin.application.usecase;

import com.postgraduate.domain.admin.application.dto.PaymentInfo;
import com.postgraduate.domain.admin.application.dto.res.PaymentManageResponse;
import com.postgraduate.domain.admin.application.mapper.AdminMapper;
import com.postgraduate.domain.payment.domain.entity.Payment;
import com.postgraduate.domain.payment.domain.service.PaymentGetService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
public class PaymentManageByAdminUseCase {
    private final PaymentGetService paymentGetService;
    public PaymentManageResponse getPayments(Integer page) {
        Page<Payment> payments = paymentGetService.all(page);
        List<PaymentInfo> paymentInfos = payments.stream()
                .map(AdminMapper::mapToPaymentInfo)
                .toList();
        Long count = paymentGetService.count();
        return new PaymentManageResponse(paymentInfos, count);
    }
}

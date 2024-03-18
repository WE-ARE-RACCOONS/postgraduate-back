package com.postgraduate.domain.adminssr.application.usecase;

import com.postgraduate.domain.admin.application.dto.PaymentInfo;
import com.postgraduate.domain.mentoring.domain.entity.Mentoring;
import com.postgraduate.domain.mentoring.domain.service.MentoringGetService;
import com.postgraduate.domain.payment.domain.entity.Payment;
import com.postgraduate.domain.payment.domain.service.PaymentGetService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static com.postgraduate.domain.admin.application.mapper.AdminMapper.mapToPaymentInfo;

@Service
@Transactional
@RequiredArgsConstructor
public class AdminPaymentUseCase {
    private final PaymentGetService paymentGetService;
    private final MentoringGetService mentoringGetService;

    public List<PaymentInfo> paymentInfos() {
        List<Payment> all = paymentGetService.all();
        return all.stream()
                .map(payment -> {
                    Mentoring mentoring = mentoringGetService.byPaymentWithNull(payment);
                    if (mentoring == null)
                        return mapToPaymentInfo(payment);
                    return mapToPaymentInfo(payment, mentoring);
                })
                .toList();
    }
}

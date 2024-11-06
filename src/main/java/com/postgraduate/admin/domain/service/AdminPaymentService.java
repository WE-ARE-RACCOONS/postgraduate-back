package com.postgraduate.admin.domain.service;

import com.postgraduate.admin.application.dto.res.PaymentWithMentoringQuery;
import com.postgraduate.admin.domain.repository.AdminPaymentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class AdminPaymentService {
    private final AdminPaymentRepository adminPaymentRepository;

    public List<PaymentWithMentoringQuery> allPayments() {
        return adminPaymentRepository.findAllPayment();
    }
}

package com.postgraduate.admin.domain.service;

import com.postgraduate.admin.application.dto.res.PaymentWithMentoringQuery;
import com.postgraduate.admin.domain.repository.AdminPaymentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AdminPaymentService {
    private final AdminPaymentRepository adminPaymentRepository;
    private static final int PAYMENT_PAGE_SIZE = 20;

    public Page<PaymentWithMentoringQuery> allPayments(Integer page) {
        if (page == null)
            page = 1;
        Pageable pageable = PageRequest.of(page-1, PAYMENT_PAGE_SIZE);
        return adminPaymentRepository.findAllPayment(pageable);
    }
}

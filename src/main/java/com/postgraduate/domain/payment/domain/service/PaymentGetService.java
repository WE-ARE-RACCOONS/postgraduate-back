package com.postgraduate.domain.payment.domain.service;

import com.postgraduate.domain.mentoring.domain.entity.Mentoring;
import com.postgraduate.domain.payment.domain.entity.Payment;
import com.postgraduate.domain.payment.domain.repository.PaymentRepository;
import com.postgraduate.domain.payment.exception.PaymentNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class PaymentGetService {
    private static final int ADMIN_PAGE_SIZE = 15;
    private final PaymentRepository paymentRepository;
    public Page<Payment> all(Integer page, String search) {
        page = page == null ? 1 : page;
        Pageable pageable = PageRequest.of(page - 1, ADMIN_PAGE_SIZE);
        search = search == null ? "" : search;
        return paymentRepository.findAllBySearchPayment(search, pageable);
    }

    public Payment byMentoring(Mentoring mentoring) {
        return paymentRepository.findByMentoring(mentoring).orElseThrow(PaymentNotFoundException::new);
    }
}

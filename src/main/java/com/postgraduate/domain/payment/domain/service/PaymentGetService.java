package com.postgraduate.domain.payment.domain.service;

import com.postgraduate.domain.mentoring.domain.entity.Mentoring;
import com.postgraduate.domain.payment.domain.entity.Payment;
import com.postgraduate.domain.payment.domain.entity.constant.Status;
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
    public Page<Payment> all(Integer page) {
        if (page == null)
            page = 1;
        Pageable pageable = PageRequest.of(page - 1, ADMIN_PAGE_SIZE);
        return paymentRepository.findAll(pageable);
    }

    public Long count() {
        return paymentRepository.countAllByStatus(Status.DONE);
    }

    public Payment byMentoring(Mentoring mentoring) {
        return paymentRepository.findByMentoring(mentoring).orElseThrow(PaymentNotFoundException::new);
    }
}

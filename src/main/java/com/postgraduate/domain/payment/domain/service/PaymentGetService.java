package com.postgraduate.domain.payment.domain.service;

import com.postgraduate.domain.mentoring.domain.entity.Mentoring;
import com.postgraduate.domain.payment.domain.entity.Payment;
import com.postgraduate.domain.payment.domain.repository.PaymentRepository;
import com.postgraduate.domain.payment.exception.PaymentNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class PaymentGetService {
    private final PaymentRepository paymentRepository;
    public List<Payment> all() {
        return paymentRepository.findAll();
    }

    public Payment byMentoring(Mentoring mentoring) {
        return paymentRepository.findByMentoring(mentoring).orElseThrow(PaymentNotFoundException::new);
    }
}

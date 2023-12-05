package com.postgraduate.domain.payment.domain.service;

import com.postgraduate.domain.mentoring.domain.entity.Mentoring;
import com.postgraduate.domain.payment.domain.entity.Payment;
import com.postgraduate.domain.payment.domain.repository.PaymentRepository;
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

    public Boolean existsByMentoring(Mentoring mentoring) {
        return paymentRepository.existsByMentoring(mentoring);
    }

    public Payment byMentoring(Mentoring mentoring) {
        return paymentRepository.findByMentoring(mentoring);
    }
}

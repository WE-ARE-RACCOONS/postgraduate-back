package com.postgraduate.domain.payment.domain.service;

import com.postgraduate.domain.payment.domain.entity.Payment;
import com.postgraduate.domain.payment.domain.repository.PaymentRepository;
import com.postgraduate.domain.payment.exception.PaymentNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class PaymentGetService {
    private final PaymentRepository paymentRepository;

    public Payment byPaymentId(Long paymentId) {
        return paymentRepository.findById(paymentId).orElseThrow(PaymentNotFoundException::new);
    }
}

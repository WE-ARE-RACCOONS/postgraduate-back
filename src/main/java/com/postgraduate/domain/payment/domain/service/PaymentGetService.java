package com.postgraduate.domain.payment.domain.service;

import com.postgraduate.domain.payment.domain.entity.Payment;
import com.postgraduate.domain.payment.domain.repository.PaymentRepository;
import com.postgraduate.domain.payment.exception.PaymentNotFoundException;
import com.postgraduate.domain.user.domain.entity.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class PaymentGetService {
    private final PaymentRepository paymentRepository;

    public Payment byOrderId(String orderId) {
        return paymentRepository.findByOrderId(orderId).orElseThrow(PaymentNotFoundException::new);
    }

    public Payment byUserAndOrderId(User user, String orderId) {
        return paymentRepository.findByUserAndOrderId(user, orderId).orElseThrow(PaymentNotFoundException::new);
    }
}

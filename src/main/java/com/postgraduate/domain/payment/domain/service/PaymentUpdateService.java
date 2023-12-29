package com.postgraduate.domain.payment.domain.service;

import com.postgraduate.domain.payment.domain.entity.constant.Status;
import com.postgraduate.domain.payment.domain.entity.Payment;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class PaymentUpdateService {

    public void updateStatus(Payment payment, Status status) {
        payment.updateStatus(status);
    }
}

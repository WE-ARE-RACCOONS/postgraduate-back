package com.postgraduate.domain.payment.domain.service;

import com.postgraduate.domain.payment.domain.entity.Payment;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import static com.postgraduate.domain.payment.domain.entity.constant.Status.CANCEL;

@Service
@RequiredArgsConstructor
public class PaymentUpdateService {

    public void updateCancel(Payment payment) {
        payment.updateStatus(CANCEL);
    }
}

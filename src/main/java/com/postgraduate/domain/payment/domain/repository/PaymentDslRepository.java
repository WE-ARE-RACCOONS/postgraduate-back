package com.postgraduate.domain.payment.domain.repository;

import com.postgraduate.domain.payment.domain.entity.Payment;

import java.util.List;

public interface PaymentDslRepository {
    List<Payment> findAllPayment();
}

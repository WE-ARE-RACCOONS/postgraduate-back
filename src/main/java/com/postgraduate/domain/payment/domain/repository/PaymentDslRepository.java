package com.postgraduate.domain.payment.domain.repository;

import com.postgraduate.domain.payment.domain.entity.Payment;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface PaymentDslRepository {
    Page<Payment> findAllBySearchPayment(String search, Pageable pageable);
}

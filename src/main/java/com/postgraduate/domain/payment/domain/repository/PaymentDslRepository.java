package com.postgraduate.domain.payment.domain.repository;

import com.postgraduate.domain.payment.domain.entity.Payment;
import com.postgraduate.domain.senior.domain.entity.Senior;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface PaymentDslRepository {
    Page<Payment> findAllBySearchPayment(String search, Pageable pageable);
    List<Payment> findAllBySeniorAndStatus(Senior senior, Boolean status);
}

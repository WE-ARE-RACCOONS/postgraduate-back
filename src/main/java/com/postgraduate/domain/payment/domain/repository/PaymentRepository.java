package com.postgraduate.domain.payment.domain.repository;

import com.postgraduate.domain.payment.domain.entity.Payment;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PaymentRepository extends JpaRepository<Payment, Long>, PaymentDslRepository {}

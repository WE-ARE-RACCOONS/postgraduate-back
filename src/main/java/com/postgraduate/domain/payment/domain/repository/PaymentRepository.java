package com.postgraduate.domain.payment.domain.repository;

import com.postgraduate.domain.payment.domain.entity.Payment;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface PaymentRepository extends JpaRepository<Payment, Long>, PaymentDslRepository {
    Optional<Payment> findByOrderId(String orderId);
}

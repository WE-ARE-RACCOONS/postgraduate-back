package com.postgraduate.domain.payment.domain.repository;

import com.postgraduate.domain.payment.domain.entity.Payment;
import com.postgraduate.domain.payment.domain.entity.constant.PaymentStatus;
import com.postgraduate.domain.senior.domain.entity.Senior;
import com.postgraduate.domain.user.user.domain.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface PaymentRepository extends JpaRepository<Payment, Long>, PaymentDslRepository {
    Optional<Payment> findByUserAndOrderIdAndStatus(User user, String orderId, PaymentStatus paymentStatus);
    Optional<Payment> findByUser_UserIdAndPaymentIdAndStatus(Long userId, Long paymentId, PaymentStatus paymentStatus);
    Optional<Payment> findBySeniorAndOrderIdAndStatus(Senior senior, String orderId, PaymentStatus paymentStatus);
    List<Payment> findAllByUser(User user);
    List<Payment> findAllBySenior(Senior senior);
}

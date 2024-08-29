package com.postgraduate.domain.payment.domain.repository;

import com.postgraduate.domain.payment.domain.entity.Payment;
import com.postgraduate.domain.payment.domain.entity.constant.Status;
import com.postgraduate.domain.member.senior.domain.entity.Senior;
import com.postgraduate.domain.member.user.domain.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface PaymentRepository extends JpaRepository<Payment, Long>, PaymentDslRepository {
    Optional<Payment> findByUserAndOrderIdAndStatus(User user, String orderId, Status status);
    Optional<Payment> findByUser_UserIdAndPaymentIdAndStatus(Long userId, Long paymentId, Status status);
    Optional<Payment> findBySeniorAndOrderIdAndStatus(Senior senior, String orderId, Status status);
    List<Payment> findAllByUser(User user);
    List<Payment> findAllBySenior(Senior senior);
}

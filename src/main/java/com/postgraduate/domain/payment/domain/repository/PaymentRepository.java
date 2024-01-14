package com.postgraduate.domain.payment.domain.repository;

import com.postgraduate.domain.mentoring.domain.entity.Mentoring;
import com.postgraduate.domain.payment.domain.entity.Payment;
import com.postgraduate.domain.payment.domain.entity.constant.Status;
import com.postgraduate.domain.salary.domain.entity.Salary;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface PaymentRepository extends JpaRepository<Payment, Long>, PaymentDslRepository {
    Optional<Payment> findByMentoring(Mentoring mentoring);
    Optional<Payment> findByMentoring_MentoringIdAndStatus(Long mentoirngId, Status status);
    List<Payment> findAllBySalary(Salary salary);
    Optional<Payment> findByPaymentIdAndStatus(Long paymentId, Status status);
}

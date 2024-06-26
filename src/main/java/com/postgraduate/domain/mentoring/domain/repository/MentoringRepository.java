package com.postgraduate.domain.mentoring.domain.repository;

import com.postgraduate.domain.mentoring.domain.entity.Mentoring;
import com.postgraduate.domain.mentoring.domain.entity.constant.Status;
import com.postgraduate.domain.payment.domain.entity.Payment;
import com.postgraduate.domain.senior.domain.entity.Senior;
import com.postgraduate.domain.user.domain.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface MentoringRepository extends JpaRepository<Mentoring, Long>, MentoringDslRepository {
    Optional<Mentoring> findByPayment(Payment payment);
    Optional<Mentoring> findByMentoringIdAndUserAndStatus(Long mentoringId, User user, Status status);
    Optional<Mentoring> findByMentoringIdAndSeniorAndStatus(Long mentoringId, Senior senior, Status status);
    Page<Mentoring> findAllByStatusAndCreatedAtBefore(Status status, LocalDateTime createdAt, Pageable pageable);
}

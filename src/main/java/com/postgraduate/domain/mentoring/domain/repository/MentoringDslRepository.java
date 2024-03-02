package com.postgraduate.domain.mentoring.domain.repository;

import com.postgraduate.domain.mentoring.domain.entity.Mentoring;
import com.postgraduate.domain.mentoring.domain.entity.constant.Status;
import com.postgraduate.domain.senior.domain.entity.Senior;
import com.postgraduate.domain.user.domain.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface MentoringDslRepository {
    List<Mentoring> findAllBySeniorId(Long seniorId);
    List<Mentoring> findAllBySeniorAndStatus(Senior senior, Status status);
    List<Mentoring> findAllBySeniorAndDone(Senior inputSenior);
    List<Mentoring> findAllByUserId(Long userId);
    List<Mentoring> findAllByUserAndStatus(User user, Status status);
    Optional<Mentoring> findByMentoringId(Long mentoringId);
    List<Mentoring> findAllBySeniorAndSalaryStatus(Senior senior, Boolean status);

    Page<Mentoring> findAllBySearchPayment(String search, Pageable pageable);

    List<Mentoring> findAllByStatusAndCreatedAtIsBefore(Status status, LocalDateTime now);

    List<Mentoring> findAllByStatus(Status status);
}

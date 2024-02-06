package com.postgraduate.domain.mentoring.domain.repository;

import com.postgraduate.domain.mentoring.domain.entity.Mentoring;
import com.postgraduate.domain.mentoring.domain.entity.constant.Status;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.List;

public interface MentoringRepository extends JpaRepository<Mentoring, Long>, MentoringDslRepository {
    List<Mentoring> findAllByStatus(Status status);
    List<Mentoring> findAllByStatusAndCreatedAtIsBefore(Status status, LocalDateTime now);
}

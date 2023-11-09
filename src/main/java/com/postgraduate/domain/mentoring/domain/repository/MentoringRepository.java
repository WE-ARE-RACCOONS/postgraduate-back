package com.postgraduate.domain.mentoring.domain.repository;

import com.postgraduate.domain.mentoring.domain.entity.Mentoring;
import com.postgraduate.domain.mentoring.domain.entity.constant.Status;
import com.postgraduate.domain.senior.domain.entity.Senior;
import com.postgraduate.domain.user.domain.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface MentoringRepository extends JpaRepository<Mentoring, Long> {
    Optional<Mentoring> findByMentoringIdAndDeletedAtIsNull(Long mentoringId);
    Optional<List<Mentoring>> findAllByUserAndStatusAndDeletedAtIsNull(User user, Status status);
    Optional<List<Mentoring>> findAllBySeniorAndStatusAndDeletedAtIsNull(Senior senior, Status status);
}

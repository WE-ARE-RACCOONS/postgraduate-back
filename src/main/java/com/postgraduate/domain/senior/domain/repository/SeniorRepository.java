package com.postgraduate.domain.senior.domain.repository;

import com.postgraduate.domain.senior.domain.entity.Senior;
import com.postgraduate.domain.senior.domain.entity.constant.Status;
import com.postgraduate.domain.user.domain.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface SeniorRepository extends JpaRepository<Senior, Long> {
    Optional<Senior> findByUser(User user);
    Optional<Senior> findBySeniorIdAndProfileNotNull(Long seniorId);
    Optional<List<Senior>> findAllByStatus(Status status);
}

package com.postgraduate.domain.senior.domain.repository;

import com.postgraduate.domain.senior.domain.entity.Senior;
import com.postgraduate.domain.senior.domain.entity.constant.Status;
import com.postgraduate.domain.user.domain.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface SeniorRepository extends JpaRepository<Senior, Long>, SeniorDslRepository {
    Optional<Senior> findByUser(User user);
    Optional<Senior> findBySeniorIdAndUser_IsDelete(Long seniorId, Boolean isDelete);
    Optional<Senior> findBySeniorIdAndProfileNotNullAndStatusAndUser_IsDelete(Long seniorId, Status status, Boolean isDelete);
    List<Senior> findAllByStatus(Status status);
    List<Senior> findAllByUser_IsDelete(Boolean isDelete);
}

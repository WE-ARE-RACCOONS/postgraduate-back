package com.postgraduate.domain.mentoring.domain.repository;

import com.postgraduate.domain.mentoring.domain.entity.Mentoring;
import com.postgraduate.domain.mentoring.domain.entity.constant.Status;
import com.postgraduate.domain.user.domain.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface MentoringRepository extends JpaRepository<Mentoring, Long>, MentoringDslRepository {
//    Optional<Mentoring> findByMentoringIdAndUser_IsDeleteAndSenior_User_IsDelete(Long mentoringId, Boolean isUserDelete, Boolean isSeniorDelete);
    List<Mentoring> findAllByUserAndStatus(User user, Status status);
    List<Mentoring> findAllByUser_UserId(Long userId);
    List<Mentoring> findAllByStatus(Status status);
    List<Mentoring> findAllByStatusAndCreatedAtIsBefore(Status status, LocalDateTime now);
}

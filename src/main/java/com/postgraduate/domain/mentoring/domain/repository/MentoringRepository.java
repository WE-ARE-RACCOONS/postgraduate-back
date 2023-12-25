package com.postgraduate.domain.mentoring.domain.repository;

import com.postgraduate.domain.mentoring.domain.entity.Mentoring;
import com.postgraduate.domain.mentoring.domain.entity.constant.Status;
import com.postgraduate.domain.senior.domain.entity.Senior;
import com.postgraduate.domain.user.domain.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface MentoringRepository extends JpaRepository<Mentoring, Long> {
    Optional<Mentoring> findByMentoringIdAndUser_IsDeleteAndSenior_User_IsDelete(Long mentoringId, Boolean isUserDelete, Boolean isSeniorDelete);
    List<Mentoring> findAllByUserAndStatus(User user, Status status);
    List<Mentoring> findAllBySeniorAndStatus(Senior senior, Status status);
    List<Mentoring> findAllByStatusAndCreatedAtIsBefore(Status status, LocalDateTime now);
    List<Mentoring> findAllByUser_UserId(Long userId);
    List<Mentoring> findAllBySenior_SeniorId(Long seniorId);
}

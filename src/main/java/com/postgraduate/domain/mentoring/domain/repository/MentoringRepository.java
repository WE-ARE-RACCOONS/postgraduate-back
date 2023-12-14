package com.postgraduate.domain.mentoring.domain.repository;

import com.postgraduate.domain.mentoring.domain.entity.Mentoring;
import com.postgraduate.domain.mentoring.domain.entity.constant.Status;
import com.postgraduate.domain.senior.domain.entity.Senior;
import com.postgraduate.domain.user.domain.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface MentoringRepository extends JpaRepository<Mentoring, Long> {
    Optional<Mentoring> findByMentoringId(Long mentoringId);
    List<Mentoring> findAllByUserAndStatusAndSenior_User_IsDelete(User user, Status status, Boolean isDelete);
    List<Mentoring> findAllBySeniorAndStatusAndUser_IsDelete(Senior senior, Status status, Boolean isDelete);
    List<Mentoring> findAllByStatusAndCreatedAtIsBefore(Status status, LocalDate now);
    List<Mentoring> findAllByUser_UserId(Long userId);
    List<Mentoring> findAllBySenior_SeniorId(Long seniorId);
}

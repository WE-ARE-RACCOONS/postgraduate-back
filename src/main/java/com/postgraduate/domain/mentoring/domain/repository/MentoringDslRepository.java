package com.postgraduate.domain.mentoring.domain.repository;

import com.postgraduate.domain.mentoring.application.dto.DoneSeniorMentoringInfo;
import com.postgraduate.domain.mentoring.domain.entity.Mentoring;
import com.postgraduate.domain.mentoring.domain.entity.constant.Status;
import com.postgraduate.domain.senior.domain.entity.Senior;
import com.postgraduate.domain.user.domain.entity.User;

import java.util.List;
import java.util.Optional;

public interface MentoringDslRepository {
    List<Mentoring> findAllBySeniorId(Long seniorId);
    List<Mentoring> findAllBySeniorAndStatus(Senior senior, Status status);
    List<DoneSeniorMentoringInfo> findAllBySeniorAndDone(Senior inputSenior);
    List<Mentoring> findAllByUserId(Long userId);
    List<Mentoring> findAllByUserAndStatus(User user, Status status);
    Optional<Mentoring> findByMentoringId(Long mentoringId);
}

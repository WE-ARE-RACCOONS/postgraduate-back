package com.postgraduate.domain.mentoring.domain.repository;

import com.postgraduate.domain.mentoring.domain.entity.Mentoring;
import com.postgraduate.domain.mentoring.domain.entity.Refuse;
import com.postgraduate.domain.mentoring.domain.entity.constant.MentoringStatus;
import com.postgraduate.domain.senior.domain.entity.Senior;
import com.postgraduate.domain.user.domain.entity.User;

import java.util.List;
import java.util.Optional;

public interface MentoringDslRepository {
    List<Mentoring> findAllBySeniorAndStatus(Senior senior, MentoringStatus mentoringStatus);
    List<Mentoring> findAllByUserAndStatus(User user, MentoringStatus mentoringStatus);
    List<Mentoring> findAllBySeniorAndSalaryStatus(Senior senior, Boolean status);
    Optional<Mentoring> findByMentoringIdAndUserForDetails(Long mentoringId, User user);
    Optional<Mentoring> findByMentoringIdAndSeniorForDetails(Long mentoringId, Senior senior);
    List<Mentoring> findAllForMessage();
    boolean existSeniorMentoring(Senior senior);
    boolean existUserMentoring(User user);
    void saveRefuse(Refuse refuse);
}

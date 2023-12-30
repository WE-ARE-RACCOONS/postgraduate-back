package com.postgraduate.domain.mentoring.domain.repository;

import com.postgraduate.domain.mentoring.domain.entity.Mentoring;
import com.postgraduate.domain.mentoring.domain.entity.constant.Status;
import com.postgraduate.domain.senior.domain.entity.Senior;

import java.util.List;

public interface MentoringDslRepository {
    List<Mentoring> findAllBySenior(Long seniorId);
    List<Mentoring> findAllBySeniorAndStatus(Senior senior, Status status);
    List<Mentoring> findAllByStatus(Status status);
}

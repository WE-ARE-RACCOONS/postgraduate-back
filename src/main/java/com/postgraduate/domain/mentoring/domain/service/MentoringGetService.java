package com.postgraduate.domain.mentoring.domain.service;

import com.postgraduate.domain.mentoring.domain.entity.Mentoring;
import com.postgraduate.domain.mentoring.domain.entity.constant.Status;
import com.postgraduate.domain.mentoring.domain.repository.MentoringRepository;
import com.postgraduate.domain.mentoring.exception.MentoringNotFoundException;
import com.postgraduate.domain.senior.domain.entity.Senior;
import com.postgraduate.domain.user.domain.entity.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class MentoringGetService {
    private final MentoringRepository mentoringRepository;

    public List<Mentoring> mentoringByUser(User user, Status status) {
        return mentoringRepository.findAllByUserAndStatus(user, status).orElse(new ArrayList<>());
    }

    public List<Mentoring> mentoringBySenior(Senior senior, Status status) {
        return mentoringRepository.findAllBySeniorAndStatus(senior, status).orElse(new ArrayList<>());
    }

    public Mentoring byMentoringId(Long mentoringId) {
        return mentoringRepository.findByMentoringId(mentoringId).orElseThrow(MentoringNotFoundException::new);
    }

//    public List<Mentoring> all() {
//        return mentoringRepository.findAllByDeletedAtIsNull().orElse(new ArrayList<>());
//    }
}

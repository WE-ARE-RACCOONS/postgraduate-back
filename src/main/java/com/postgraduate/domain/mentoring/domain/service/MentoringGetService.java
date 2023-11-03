package com.postgraduate.domain.mentoring.domain.service;

import com.postgraduate.domain.mentoring.domain.entity.Mentoring;
import com.postgraduate.domain.mentoring.domain.entity.constant.Status;
import com.postgraduate.domain.mentoring.domain.repository.MentoringRepository;
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
        List<Mentoring> mentorings =
                mentoringRepository.findAllByUserAndStatus(user, status).orElse(new ArrayList<>());
        return mentorings;
    }

    public Mentoring byMentoringId(Long mentoringId) {
        return mentoringRepository.findById(mentoringId).orElseThrow();
    }
}

package com.postgraduate.domain.mentoring.domain.service;

import com.postgraduate.domain.mentoring.domain.entity.Mentoring;
import com.postgraduate.domain.mentoring.domain.repository.MentoringRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class MentoringSaveService {
    private final MentoringRepository mentoringRepository;

    public Mentoring save(Mentoring mentoring) {
        return mentoringRepository.save(mentoring);
    }
}

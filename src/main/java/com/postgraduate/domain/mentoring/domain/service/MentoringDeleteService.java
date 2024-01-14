package com.postgraduate.domain.mentoring.domain.service;

import com.postgraduate.domain.mentoring.domain.entity.Mentoring;
import com.postgraduate.domain.mentoring.domain.repository.MentoringRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class MentoringDeleteService {
    private final MentoringRepository mentoringRepository;

    public void deleteMentoring(Mentoring mentoring) {
        mentoringRepository.deleteById(mentoring.getMentoringId());
    }
}

package com.postgraduate.domain.mentoring.application.usecase;

import com.postgraduate.domain.mentoring.domain.entity.Mentoring;
import com.postgraduate.domain.mentoring.domain.service.MentoringGetService;
import com.postgraduate.domain.senior.domain.entity.Senior;
import com.postgraduate.domain.senior.domain.service.SeniorGetService;
import com.postgraduate.domain.user.domain.entity.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@RequiredArgsConstructor
public class CheckIsMyMentoringUseCase {
    private final MentoringGetService mentoringGetService;
    private final SeniorGetService seniorGetService;
    public Mentoring checkByRole(User user, Long mentoringId) {
        Mentoring mentoring = mentoringGetService.byMentoringId(mentoringId);

        Senior senior = seniorGetService.byUser(user);
        if (mentoring.getUser() != user && mentoring.getSenior() != senior) {
            throw new IllegalArgumentException("권한없음");
        }
        return mentoring;
    }
}


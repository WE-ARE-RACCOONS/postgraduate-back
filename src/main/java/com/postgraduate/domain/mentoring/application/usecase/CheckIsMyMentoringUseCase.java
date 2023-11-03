package com.postgraduate.domain.mentoring.application.usecase;

import com.postgraduate.domain.auth.exception.PermissionDeniedException;
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
        if (mentoring.getUser() != user) {
            throw new PermissionDeniedException();
        }
        return mentoring;
    }

    public Mentoring checkByRole(Senior senior, Long mentoringId) {
        Mentoring mentoring = mentoringGetService.byMentoringId(mentoringId);
        if (mentoring.getSenior() != senior) {
            throw new PermissionDeniedException();
        }
        return mentoring;
    }
}


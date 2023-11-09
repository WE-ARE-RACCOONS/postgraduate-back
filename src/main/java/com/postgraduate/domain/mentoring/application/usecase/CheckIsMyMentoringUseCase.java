package com.postgraduate.domain.mentoring.application.usecase;

import com.postgraduate.domain.auth.exception.PermissionDeniedException;
import com.postgraduate.domain.mentoring.domain.entity.Mentoring;
import com.postgraduate.domain.mentoring.domain.service.MentoringGetService;
import com.postgraduate.domain.senior.domain.entity.Senior;
import com.postgraduate.domain.senior.domain.service.SeniorGetService;
import com.postgraduate.domain.user.domain.entity.User;
import com.postgraduate.global.auth.AuthDetails;
import com.postgraduate.global.config.security.util.SecurityUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@RequiredArgsConstructor
public class CheckIsMyMentoringUseCase {
    private final SecurityUtils securityUtils;
    private final SeniorGetService seniorGetService;
    private final MentoringGetService mentoringGetService;

    public Mentoring byUser(AuthDetails authDetails, Long mentoringId) {
        User user = securityUtils.getLoggedInUser(authDetails);
        Mentoring mentoring = mentoringGetService.byMentoringId(mentoringId);
        if (mentoring.getUser() != user) {
            throw new PermissionDeniedException();
        }
        return mentoring;
    }

    public Mentoring bySenior(AuthDetails authDetails, Long mentoringId) {
        User user = securityUtils.getLoggedInUser(authDetails);
        Senior senior = seniorGetService.byUser(user);
        Mentoring mentoring = mentoringGetService.byMentoringId(mentoringId);
        if (mentoring.getSenior() != senior) {
            throw new PermissionDeniedException();
        }
        return mentoring;
    }
}


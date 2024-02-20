package com.postgraduate.domain.mentoring.application.usecase;

import com.postgraduate.domain.auth.exception.PermissionDeniedException;
import com.postgraduate.domain.mentoring.domain.entity.Mentoring;
import com.postgraduate.domain.mentoring.domain.service.MentoringGetService;
import com.postgraduate.domain.senior.domain.entity.Senior;
import com.postgraduate.domain.senior.domain.service.SeniorGetService;
import com.postgraduate.domain.user.domain.entity.User;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Slf4j
public class CheckIsMyMentoringUseCase {
    private final MentoringGetService mentoringGetService;

    public Mentoring byUser(User user, Long mentoringId) {
        Mentoring mentoring = mentoringGetService.byMentoringId(mentoringId);
        if (!mentoring.getUser().isEqual(user)) {
            log.warn("userId = {}", user.getUserId());
            log.warn("mentoring.getUserId = {}", mentoring.getUser().getUserId());
            throw new PermissionDeniedException();
        }
        return mentoring;
    }

    public Mentoring bySenior(Senior senior, Long mentoringId) {
        Mentoring mentoring = mentoringGetService.byMentoringId(mentoringId);
        if (!mentoring.getSenior().isEqual(senior)) {
            log.warn("userId = {}", senior.getSeniorId());
            log.warn("mentoring.getUserId = {}", mentoring.getSenior().getSeniorId());
            throw new PermissionDeniedException();
        }
        return mentoring;
    }
}


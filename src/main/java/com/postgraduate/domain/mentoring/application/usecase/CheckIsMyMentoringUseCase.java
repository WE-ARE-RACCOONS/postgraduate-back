package com.postgraduate.domain.mentoring.application.usecase;

import com.postgraduate.domain.mentoring.domain.entity.Mentoring;
import com.postgraduate.domain.mentoring.domain.service.MentoringGetService;
import com.postgraduate.domain.senior.domain.entity.Senior;
import com.postgraduate.domain.senior.domain.service.SeniorGetService;
import com.postgraduate.domain.user.domain.entity.User;
import com.postgraduate.domain.user.domain.entity.constant.Role;
import com.postgraduate.global.auth.AuthDetails;
import com.postgraduate.global.config.security.util.SecurityUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@RequiredArgsConstructor
public class CheckIsMyMentoringUseCase {
    private final MentoringGetService mentoringGetService;
    private final SecurityUtils securityUtils;
    private final SeniorGetService seniorGetService;
    public Mentoring checkByRole(AuthDetails authDetails, Long mentoringId) {
        Role role = authDetails.getRole();
        User user = securityUtils.getLoggedInUser(authDetails);
        Mentoring mentoring = mentoringGetService.byMentoringId(mentoringId);

        switch (role) {
            case ADMIN -> {}
            case USER -> {
                if (mentoring.getUser() != user) {
                    throw new IllegalArgumentException("대학생으로서 권한없음");
                }
            }
            case SENIOR -> {
                Senior senior = seniorGetService.byUser(user);
                if (mentoring.getSenior() != senior) {
                    throw new IllegalArgumentException("대학원생으로서 권한없음");
                }
            }
            default -> throw new IllegalArgumentException("권한없음");
        }
        return mentoring;
    }
}


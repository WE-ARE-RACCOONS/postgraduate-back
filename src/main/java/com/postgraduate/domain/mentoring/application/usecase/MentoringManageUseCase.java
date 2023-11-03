package com.postgraduate.domain.mentoring.application.usecase;

import com.postgraduate.domain.mentoring.application.dto.req.MentoringStatusRequest;
import com.postgraduate.domain.mentoring.domain.entity.Mentoring;
import com.postgraduate.domain.mentoring.domain.entity.constant.Status;
import com.postgraduate.domain.mentoring.domain.service.MentoringUpdateService;
import com.postgraduate.domain.user.domain.entity.User;
import com.postgraduate.global.auth.AuthDetails;
import com.postgraduate.global.config.security.util.SecurityUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@RequiredArgsConstructor
public class MentoringUpdateUseCase {
    private final SecurityUtils securityUtils;
    private final MentoringUpdateService mentoringUpdateService;
    private final CheckIsMyMentoringUseCase checkIsMyMentoringUseCase;

    public void updateStatus(AuthDetails authDetails, Long mentoringId, MentoringStatusRequest request) {
        User user = securityUtils.getLoggedInUser(authDetails);
        Mentoring mentoring = checkIsMyMentoringUseCase.checkByRole(user, mentoringId);
        mentoringUpdateService.updateStatus(mentoring, request.getStatus());
    }
}

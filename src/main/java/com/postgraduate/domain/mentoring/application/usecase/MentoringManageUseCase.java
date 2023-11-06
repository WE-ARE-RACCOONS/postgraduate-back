package com.postgraduate.domain.mentoring.application.usecase;

import com.postgraduate.domain.mentoring.application.dto.req.MentoringDateRequest;
import com.postgraduate.domain.mentoring.application.dto.req.MentoringRefuseRequest;
import com.postgraduate.domain.mentoring.application.dto.req.MentoringStatusRequest;
import com.postgraduate.domain.mentoring.domain.entity.Mentoring;
import com.postgraduate.domain.mentoring.domain.service.MentoringUpdateService;
import com.postgraduate.domain.mentoring.exception.MentoringNotWaitingException;
import com.postgraduate.domain.senior.domain.entity.Senior;
import com.postgraduate.domain.senior.domain.service.SeniorGetService;
import com.postgraduate.domain.user.domain.entity.User;
import com.postgraduate.global.auth.AuthDetails;
import com.postgraduate.global.config.security.util.SecurityUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import static com.postgraduate.domain.mentoring.domain.entity.constant.Status.WAITING;

@Service
@Transactional
@RequiredArgsConstructor
public class MentoringManageUseCase {
    private final SecurityUtils securityUtils;
    private final MentoringUpdateService mentoringUpdateService;
    private final SeniorGetService seniorGetService;
    private final CheckIsMyMentoringUseCase checkIsMyMentoringUseCase;

    public void updateStatus(AuthDetails authDetails, Long mentoringId, MentoringStatusRequest request) {
        User user = securityUtils.getLoggedInUser(authDetails);
        Mentoring mentoring = checkIsMyMentoringUseCase.checkByRole(user, mentoringId);
        mentoringUpdateService.updateStatus(mentoring, request.getStatus());
    }

    public void updateSeniorStatus(AuthDetails authDetails, Long mentoringId, MentoringStatusRequest request) {
        User user = securityUtils.getLoggedInUser(authDetails);
        Senior senior = seniorGetService.byUser(user);
        Mentoring mentoring = checkIsMyMentoringUseCase.checkByRole(senior, mentoringId);
        mentoringUpdateService.updateStatus(mentoring, request.getStatus());
    }

    public void updateRefuse(AuthDetails authDetails, Long mentoringId, MentoringRefuseRequest request) {
        User user = securityUtils.getLoggedInUser(authDetails);
        Senior senior = seniorGetService.byUser(user);
        Mentoring mentoring = checkIsMyMentoringUseCase.checkByRole(senior, mentoringId);
        mentoringUpdateService.updateStatus(mentoring, request.getRefuse());
    }

    public void updateDate(AuthDetails authDetails, Long mentoringId, MentoringDateRequest request) {
        User user = securityUtils.getLoggedInUser(authDetails);
        Senior senior = seniorGetService.byUser(user);
        Mentoring mentoring = checkIsMyMentoringUseCase.checkByRole(senior, mentoringId);
        if (mentoring.getStatus() != WAITING) {
            throw new MentoringNotWaitingException();
        }
        mentoringUpdateService.updateDate(mentoring, request.getDate());
    }
}

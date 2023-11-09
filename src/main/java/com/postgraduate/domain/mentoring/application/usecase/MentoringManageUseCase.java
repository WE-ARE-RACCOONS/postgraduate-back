package com.postgraduate.domain.mentoring.application.usecase;

import com.postgraduate.domain.mentoring.application.dto.req.MentoringDateRequest;
import com.postgraduate.domain.mentoring.application.dto.req.MentoringRefuseRequest;
import com.postgraduate.domain.mentoring.domain.entity.Mentoring;
import com.postgraduate.domain.mentoring.domain.entity.constant.Status;
import com.postgraduate.domain.mentoring.domain.service.MentoringUpdateService;
import com.postgraduate.domain.mentoring.exception.MentoringNotWaitingException;
import com.postgraduate.global.auth.AuthDetails;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import static com.postgraduate.domain.mentoring.domain.entity.constant.Status.WAITING;

@Service
@Transactional
@RequiredArgsConstructor
public class MentoringManageUseCase {
    private final CheckIsMyMentoringUseCase checkIsMyMentoringUseCase;
    private final MentoringUpdateService mentoringUpdateService;

    public void updateStatus(AuthDetails authDetails, Long mentoringId, Status status) {
        Mentoring mentoring = checkIsMyMentoringUseCase.byUser(authDetails, mentoringId);
        mentoringUpdateService.updateStatus(mentoring, status);
    }

    public void updateSeniorStatus(AuthDetails authDetails, Long mentoringId, Status status) {
        Mentoring mentoring = checkIsMyMentoringUseCase.bySenior(authDetails, mentoringId);
        mentoringUpdateService.updateStatus(mentoring, status);
    }

    public void updateRefuse(AuthDetails authDetails, Long mentoringId, MentoringRefuseRequest request, Status status) {
        Mentoring mentoring = checkIsMyMentoringUseCase.bySenior(authDetails, mentoringId);
        mentoringUpdateService.updateRefuse(mentoring, request.getRefuse());
        mentoringUpdateService.updateStatus(mentoring, status);
    }

    public void updateDate(AuthDetails authDetails, Long mentoringId, MentoringDateRequest request) {
        Mentoring mentoring = checkIsMyMentoringUseCase.bySenior(authDetails, mentoringId);
        if (mentoring.getStatus() != WAITING) {
            throw new MentoringNotWaitingException();
        }
        mentoringUpdateService.updateDate(mentoring, request.getDate());
    }
}

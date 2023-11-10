package com.postgraduate.domain.mentoring.application.usecase;

import com.postgraduate.domain.mentoring.application.dto.req.MentoringDateRequest;
import com.postgraduate.domain.mentoring.application.dto.req.MentoringRefuseRequest;
import com.postgraduate.domain.mentoring.domain.entity.Mentoring;
import com.postgraduate.domain.mentoring.domain.entity.constant.Status;
import com.postgraduate.domain.mentoring.domain.service.MentoringUpdateService;
import com.postgraduate.domain.mentoring.exception.MentoringNotWaitingException;
import com.postgraduate.domain.user.domain.entity.User;
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

    public void updateStatus(User user, Long mentoringId, Status status) {
        Mentoring mentoring = checkIsMyMentoringUseCase.byUser(user, mentoringId);
        mentoringUpdateService.updateStatus(mentoring, status);
    }

    public void updateSeniorStatus(User user, Long mentoringId, Status status) {
        Mentoring mentoring = checkIsMyMentoringUseCase.bySenior(user, mentoringId);
        mentoringUpdateService.updateStatus(mentoring, status);
    }

    public void updateRefuse(User user, Long mentoringId, MentoringRefuseRequest request, Status status) {
        Mentoring mentoring = checkIsMyMentoringUseCase.bySenior(user, mentoringId);
        mentoringUpdateService.updateRefuse(mentoring, request.getRefuse());
        mentoringUpdateService.updateStatus(mentoring, status);
    }

    public void updateDate(User user, Long mentoringId, MentoringDateRequest request) {
        Mentoring mentoring = checkIsMyMentoringUseCase.bySenior(user, mentoringId);
        if (mentoring.getStatus() != WAITING) {
            throw new MentoringNotWaitingException();
        }
        mentoringUpdateService.updateDate(mentoring, request.getDate());
    }
}

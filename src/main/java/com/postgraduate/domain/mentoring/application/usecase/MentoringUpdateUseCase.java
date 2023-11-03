package com.postgraduate.domain.mentoring.application.usecase;

import com.postgraduate.domain.mentoring.application.dto.req.MentoringStatusRequest;
import com.postgraduate.domain.mentoring.domain.entity.Mentoring;
import com.postgraduate.domain.mentoring.domain.entity.constant.Status;
import com.postgraduate.domain.mentoring.domain.service.MentoringUpdateService;
import com.postgraduate.global.auth.AuthDetails;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@RequiredArgsConstructor
public class MentoringUpdateUseCase {
    private final MentoringUpdateService mentoringUpdateService;
    private final CheckIsMyMentoringUseCase checkIsMyMentoringUseCase;

    public void updateStatus(AuthDetails authDetails, Long mentoringId, MentoringStatusRequest request) {
        Mentoring mentoring = checkIsMyMentoringUseCase.checkByRole(authDetails, mentoringId);
        mentoringUpdateService.updateStatus(mentoring, Status.valueOf(request.getStatus()));
    }
}

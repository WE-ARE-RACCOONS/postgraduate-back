package com.postgraduate.domain.mentoring.application.usecase;

import com.postgraduate.domain.mentoring.application.dto.DoneMentoringInfo;
import com.postgraduate.domain.mentoring.application.dto.ExpectedMentoringInfo;
import com.postgraduate.domain.mentoring.application.dto.WaitingMentoringInfo;
import com.postgraduate.domain.mentoring.application.dto.res.AppliedMentoringDetailResponse;
import com.postgraduate.domain.mentoring.application.dto.res.AppliedMentoringResponse;
import com.postgraduate.domain.mentoring.application.mapper.MentoringMapper;
import com.postgraduate.domain.mentoring.domain.entity.Mentoring;
import com.postgraduate.domain.mentoring.domain.service.MentoringGetService;
import com.postgraduate.domain.mentoring.exception.MentoringDetailNotFoundException;
import com.postgraduate.domain.user.domain.entity.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static com.postgraduate.domain.mentoring.application.mapper.MentoringMapper.*;
import static com.postgraduate.domain.mentoring.domain.entity.constant.Status.*;

@Service
@Transactional
@RequiredArgsConstructor
public class MentoringUserInfoUseCase {
    private final MentoringGetService mentoringGetService;
    private final CheckIsMyMentoringUseCase checkIsMyMentoringUseCase;

    public AppliedMentoringDetailResponse getMentoringDetail(User user, Long mentoringId) {
        Mentoring mentoring = checkIsMyMentoringUseCase.byUser(user, mentoringId);
        if (!(mentoring.getStatus() == WAITING || mentoring.getStatus() == EXPECTED)) {
            throw new MentoringDetailNotFoundException();
        }
        return mapToAppliedDetailInfo(mentoring);
    }

    public AppliedMentoringResponse getWaiting(User user) {
        List<Mentoring> mentorings = mentoringGetService.mentoringByUser(user, WAITING);
        List<WaitingMentoringInfo> waitingMentoringInfos = mentorings.stream()
                .map(MentoringMapper::mapToWaitingInfo)
                .toList();
        return new AppliedMentoringResponse(waitingMentoringInfos);
    }

    public AppliedMentoringResponse getExpected(User user) {
        List<Mentoring> mentorings = mentoringGetService.mentoringByUser(user, EXPECTED);
        List<ExpectedMentoringInfo> expectedMentoringInfos = mentorings.stream()
                .map(MentoringMapper::mapToExpectedInfo)
                .toList();
        return new AppliedMentoringResponse(expectedMentoringInfos);
    }

    public AppliedMentoringResponse getDone(User user) {
        List<Mentoring> mentorings = mentoringGetService.mentoringByUser(user, DONE);
        List<DoneMentoringInfo> doneMentoringInfos = mentorings.stream()
                .map(MentoringMapper::mapToDoneInfo)
                .toList();
        return new AppliedMentoringResponse(doneMentoringInfos);
    }
}

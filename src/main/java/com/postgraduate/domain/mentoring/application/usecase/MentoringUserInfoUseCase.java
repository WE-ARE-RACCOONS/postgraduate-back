package com.postgraduate.domain.mentoring.application.usecase;

import com.postgraduate.domain.mentoring.application.dto.DoneMentoringInfo;
import com.postgraduate.domain.mentoring.application.dto.ExpectedMentoringInfo;
import com.postgraduate.domain.mentoring.application.dto.WaitingMentoringInfo;
import com.postgraduate.domain.mentoring.application.dto.res.*;
import com.postgraduate.domain.mentoring.application.mapper.MentoringMapper;
import com.postgraduate.domain.mentoring.domain.entity.Mentoring;
import com.postgraduate.domain.mentoring.domain.service.MentoringGetService;
import com.postgraduate.domain.user.domain.entity.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static com.postgraduate.domain.mentoring.application.mapper.MentoringMapper.mapToAppliedDetailInfo;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class MentoringUserInfoUseCase {
    private final MentoringGetService mentoringGetService;

    public AppliedMentoringDetailResponse getMentoringDetail(User user, Long mentoringId) {
        Mentoring mentoring = mentoringGetService.byIdAndUserForDetails(mentoringId, user);
        return mapToAppliedDetailInfo(mentoring);
    }

    public WaitingMentoringResponse getWaiting(User user) {
        List<Mentoring> mentorings = mentoringGetService.byUserWaiting(user);
        List<WaitingMentoringInfo> waitingMentoringInfos = mentorings.stream()
                .map(MentoringMapper::mapToWaitingInfo)
                .toList();
        return new WaitingMentoringResponse(waitingMentoringInfos);
    }

    public ExpectedMentoringResponse getExpected(User user) {
        List<Mentoring> mentorings = mentoringGetService.byUserExpected(user);
        List<ExpectedMentoringInfo> expectedMentoringInfos = mentorings.stream()
                .map(MentoringMapper::mapToExpectedInfo)
                .toList();
        return new ExpectedMentoringResponse(expectedMentoringInfos);
    }

    public DoneMentoringResponse getDone(User user) {
        List<Mentoring> mentorings = mentoringGetService.byUserDone(user);
        List<DoneMentoringInfo> doneMentoringInfos = mentorings.stream()
                .map(MentoringMapper::mapToDoneInfo)
                .toList();
        return new DoneMentoringResponse(doneMentoringInfos);
    }
}

package com.postgraduate.domain.mentoring.application.usecase;

import com.postgraduate.domain.mentoring.application.dto.DoneMentoringInfo;
import com.postgraduate.domain.mentoring.application.dto.ExpectedMentoringInfo;
import com.postgraduate.domain.mentoring.application.dto.WaitingMentoringInfo;
import com.postgraduate.domain.mentoring.application.dto.res.AppliedMentoringDetailResponse;
import com.postgraduate.domain.mentoring.application.dto.res.AppliedMentoringResponse;
import com.postgraduate.domain.mentoring.domain.entity.Mentoring;
import com.postgraduate.domain.mentoring.domain.entity.constant.Status;
import com.postgraduate.domain.mentoring.domain.service.MentoringGetService;
import com.postgraduate.domain.mentoring.exception.MentoringDoneException;
import com.postgraduate.domain.mentoring.exception.MentoringNotWaitingException;
import com.postgraduate.domain.user.domain.entity.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
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
        if (mentoring.getStatus() == DONE) {
            throw new MentoringDoneException();
        }
        return mapToAppliedDetailInfo(mentoring);
    }

    public AppliedMentoringResponse getMentorings(Status status, User user) {
        List<Mentoring> mentorings = mentoringGetService.mentoringByUser(user, status);
        return getCategories(status, mentorings);
    }

    private AppliedMentoringResponse getCategories(Status status, List<Mentoring> mentorings) {
        if (status == WAITING) {
            return getWaiting(mentorings);
        }
        if (status == EXPECTED) {
            return getExpected(mentorings);
        }
        return getDone(mentorings);
    }

    private AppliedMentoringResponse getWaiting(List<Mentoring> mentorings) {
        List<WaitingMentoringInfo> waitingMentoringInfos = new ArrayList<>();
        for (Mentoring mentoring : mentorings) {
            waitingMentoringInfos.add(mapToWaitingInfo(mentoring));
        }
        return new AppliedMentoringResponse(waitingMentoringInfos);
    }

    private AppliedMentoringResponse getExpected(List<Mentoring> mentorings) {
        List<ExpectedMentoringInfo> expectedMentoringInfos = new ArrayList<>();
        for (Mentoring mentoring : mentorings) {
            expectedMentoringInfos.add(mapToExpectedInfo(mentoring));
        }
        return new AppliedMentoringResponse(expectedMentoringInfos);
    }

    private AppliedMentoringResponse getDone(List<Mentoring> mentorings) {
        List<DoneMentoringInfo> doneMentoringInfos = new ArrayList<>();
        for (Mentoring mentoring : mentorings) {
            doneMentoringInfos.add(mapToDoneInfo(mentoring));
        }
        return new AppliedMentoringResponse(doneMentoringInfos);
    }
}

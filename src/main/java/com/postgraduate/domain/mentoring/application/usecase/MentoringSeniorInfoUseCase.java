package com.postgraduate.domain.mentoring.application.usecase;

import com.postgraduate.domain.mentoring.application.dto.DoneSeniorMentoringInfo;
import com.postgraduate.domain.mentoring.application.dto.ExpectedSeniorMentoringInfo;
import com.postgraduate.domain.mentoring.application.dto.WaitingSeniorMentoringInfo;
import com.postgraduate.domain.mentoring.application.dto.res.AppliedMentoringResponse;
import com.postgraduate.domain.mentoring.application.dto.res.SeniorMentoringDetailResponse;
import com.postgraduate.domain.mentoring.domain.entity.Mentoring;
import com.postgraduate.domain.mentoring.domain.entity.constant.Status;
import com.postgraduate.domain.mentoring.domain.service.MentoringGetService;
import com.postgraduate.domain.mentoring.exception.MentoringDoneException;
import com.postgraduate.domain.senior.domain.entity.Senior;
import com.postgraduate.domain.senior.domain.service.SeniorGetService;
import com.postgraduate.domain.user.domain.entity.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static com.postgraduate.domain.mentoring.application.mapper.MentoringMapper.*;
import static com.postgraduate.domain.mentoring.application.mapper.MentoringMapper.mapToSeniorDoneInfo;
import static com.postgraduate.domain.mentoring.domain.entity.constant.Status.*;

@Service
@Transactional
@RequiredArgsConstructor
public class MentoringSeniorInfoUseCase {
    private final MentoringGetService mentoringGetService;
    private final CheckIsMyMentoringUseCase checkIsMyMentoringUseCase;
    private final SeniorGetService seniorGetService;

    public SeniorMentoringDetailResponse getSeniorMentoringDetail(User user, Long mentoringId) {
        Mentoring mentoring = checkIsMyMentoringUseCase.bySenior(user, mentoringId);
        if (mentoring.getStatus() == DONE) {
            throw new MentoringDoneException();
        }
        return mapToSeniorMentoringDetail(mentoring);
    }

    public AppliedMentoringResponse getSeniorMentorings(Status status, User user) {
        Senior senior = seniorGetService.byUser(user);
        List<Mentoring> mentorings = mentoringGetService.mentoringBySenior(senior, status);
        return getSeniorCategories(status, mentorings);
    }

    private AppliedMentoringResponse getSeniorCategories(Status status, List<Mentoring> mentorings) {
        if (status == WAITING) {
            return getSeniorWaiting(mentorings);
        }
        if (status == EXPECTED) {
            return getSeniorExpected(mentorings);
        }
        return getSeniorDone(mentorings);
    }

    private AppliedMentoringResponse getSeniorWaiting(List<Mentoring> mentorings) {
        List<WaitingSeniorMentoringInfo> waitingMentoringInfos = new ArrayList<>();
        for (Mentoring mentoring : mentorings) {
            int expiredAt = mentoring.getCreatedAt()
                    .plusDays(2)
                    .atStartOfDay()
                    .getMinute();
            int now = LocalDateTime.now()
                    .getMinute();
            waitingMentoringInfos.add(mapToSeniorWaitingInfo(mentoring, (expiredAt - now)));
        }
        return new AppliedMentoringResponse(waitingMentoringInfos);
    }

    private AppliedMentoringResponse getSeniorExpected(List<Mentoring> mentorings) {
        List<ExpectedSeniorMentoringInfo> expectedMentoringInfos = new ArrayList<>();
        for (Mentoring mentoring : mentorings) {
            expectedMentoringInfos.add(mapToSeniorExpectedInfo(mentoring));
        }
        return new AppliedMentoringResponse(expectedMentoringInfos);
    }

    private AppliedMentoringResponse getSeniorDone(List<Mentoring> mentorings) {
        List<DoneSeniorMentoringInfo> doneMentoringInfos = new ArrayList<>();
        for (Mentoring mentoring : mentorings) {
            doneMentoringInfos.add(mapToSeniorDoneInfo(mentoring, mentoring.getDate(), false)); //todo : 정산 관련 로직 필요
        }
        return new AppliedMentoringResponse(doneMentoringInfos);
    }
}

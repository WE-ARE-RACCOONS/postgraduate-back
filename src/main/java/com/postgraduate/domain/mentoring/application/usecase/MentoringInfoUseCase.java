package com.postgraduate.domain.mentoring.application.usecase;

import com.postgraduate.domain.mentoring.application.dto.DoneMentoringInfo;
import com.postgraduate.domain.mentoring.application.dto.ExpectedMentoringInfo;
import com.postgraduate.domain.mentoring.application.dto.WaitingMentoringInfo;
import com.postgraduate.domain.mentoring.application.dto.res.AppliedMentoringDetailResponse;
import com.postgraduate.domain.mentoring.application.dto.res.AppliedMentoringResponse;
import com.postgraduate.domain.mentoring.application.dto.res.SeniorMentoringDetailResponse;
import com.postgraduate.domain.mentoring.application.dto.res.SeniorMentoringResponse;
import com.postgraduate.domain.mentoring.application.mapper.MentoringMapper;
import com.postgraduate.domain.mentoring.domain.entity.Mentoring;
import com.postgraduate.domain.mentoring.domain.entity.constant.Status;
import com.postgraduate.domain.mentoring.domain.service.MentoringGetService;
import com.postgraduate.domain.mentoring.exception.MentoringDoneException;
import com.postgraduate.domain.mentoring.exception.MentoringNotWaitingException;
import com.postgraduate.domain.senior.domain.entity.Senior;
import com.postgraduate.domain.senior.domain.service.SeniorGetService;
import com.postgraduate.domain.user.domain.entity.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import static com.postgraduate.domain.mentoring.domain.entity.constant.Status.*;

@Service
@Transactional
@RequiredArgsConstructor
public class MentoringInfoUseCase {
    private final MentoringGetService mentoringGetService;
    private final CheckIsMyMentoringUseCase checkIsMyMentoringUseCase;
    private final SeniorGetService seniorGetService;


    public AppliedMentoringDetailResponse getMentoringDetail(User user, Long mentoringId) {
        Mentoring mentoring = checkIsMyMentoringUseCase.byUser(user, mentoringId);
        if (mentoring.getStatus() != WAITING) {
            throw new MentoringNotWaitingException();
        }
        return MentoringMapper.mapToAppliedDetailInfo(mentoring);
    }

    public SeniorMentoringDetailResponse getSeniorMentoringDetail(User user, Long mentoringId) {
        Mentoring mentoring = checkIsMyMentoringUseCase.bySenior(user, mentoringId);
        if (mentoring.getStatus() == DONE) {
            throw new MentoringDoneException();
        }
        return MentoringMapper.mapToSeniorMentoringDetail(mentoring);
    }

    public List<SeniorMentoringResponse> getSeniorMentorings(Status status, User user) {
        Senior senior = seniorGetService.byUser(user);

        List<Mentoring> mentorings = mentoringGetService.mentoringBySenior(senior, status);
        return mentorings.stream().map(MentoringMapper::mapToSeniorMentoring).collect(Collectors.toList());
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
            waitingMentoringInfos.add(MentoringMapper.mapToWaitingInfo(mentoring));
        }
        return new AppliedMentoringResponse(waitingMentoringInfos);
    }

    private AppliedMentoringResponse getExpected(List<Mentoring> mentorings) {
        List<ExpectedMentoringInfo> expectedMentoringInfos = new ArrayList<>();
        for (Mentoring mentoring : mentorings) {
            expectedMentoringInfos.add(MentoringMapper.mapToExpectedInfo(mentoring));
        }
        return new AppliedMentoringResponse(expectedMentoringInfos);
    }

    private AppliedMentoringResponse getDone(List<Mentoring> mentorings) {
        List<DoneMentoringInfo> doneMentoringInfos = new ArrayList<>();
        for (Mentoring mentoring : mentorings) {
            doneMentoringInfos.add(MentoringMapper.mapToDoneInfo(mentoring));
        }
        return new AppliedMentoringResponse(doneMentoringInfos);
    }
}

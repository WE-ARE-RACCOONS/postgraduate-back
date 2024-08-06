package com.postgraduate.domain.mentoring.application.usecase;

import com.postgraduate.domain.mentoring.application.dto.DoneMentoringInfo;
import com.postgraduate.domain.mentoring.application.dto.ExpectedMentoringInfo;
import com.postgraduate.domain.mentoring.application.dto.WaitingMentoringInfo;
import com.postgraduate.domain.mentoring.application.dto.res.AppliedMentoringDetailResponse;
import com.postgraduate.domain.mentoring.application.dto.res.DoneMentoringResponse;
import com.postgraduate.domain.mentoring.application.dto.res.ExpectedMentoringResponse;
import com.postgraduate.domain.mentoring.application.dto.res.WaitingMentoringResponse;
import com.postgraduate.domain.mentoring.application.mapper.MentoringMapper;
import com.postgraduate.domain.mentoring.domain.entity.Mentoring;
import com.postgraduate.domain.mentoring.domain.service.MentoringGetService;
import com.postgraduate.domain.user.user.domain.entity.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static com.postgraduate.domain.mentoring.util.DateUtils.stringToLocalDateTime;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class MentoringUserInfoUseCase {
    private final MentoringGetService mentoringGetService;
    private final MentoringMapper mentoringMapper;

    public AppliedMentoringDetailResponse getMentoringDetail(User user, Long mentoringId) {
        Mentoring mentoring = mentoringGetService.byIdAndUserForDetails(mentoringId, user);
        return mentoringMapper.mapToAppliedDetailInfo(mentoring);
    }

    public WaitingMentoringResponse getWaiting(User user) {
        List<Mentoring> mentorings = mentoringGetService.byUserWaiting(user);
        List<WaitingMentoringInfo> waitingMentoringInfos = mentorings.stream()
                .map(mentoring -> mentoringMapper.mapToWaitingInfo(mentoring))
                .toList();
        return new WaitingMentoringResponse(waitingMentoringInfos);
    }

    public ExpectedMentoringResponse getExpected(User user) {
        List<Mentoring> mentorings = mentoringGetService.byUserExpected(user);
        List<ExpectedMentoringInfo> expectedMentoringInfos = mentorings.stream()
                .map(mentoring -> mentoringMapper.mapToExpectedInfo(mentoring))
                .toList();
        return new ExpectedMentoringResponse(expectedMentoringInfos);
    }

    public DoneMentoringResponse getDone(User user) {
        List<Mentoring> mentorings = mentoringGetService.byUserDone(user);
        List<DoneMentoringInfo> doneMentoringInfos = mentorings.stream()
                .map(mentoring -> mentoringMapper.mapToDoneInfo(mentoring))
                .sorted((o1, o2) -> {
                    if (stringToLocalDateTime(o1.date())
                            .isAfter(stringToLocalDateTime(o2.date())))
                        return -1;
                    return 1;
                })
                .toList();
        return new DoneMentoringResponse(doneMentoringInfos);
    }
}

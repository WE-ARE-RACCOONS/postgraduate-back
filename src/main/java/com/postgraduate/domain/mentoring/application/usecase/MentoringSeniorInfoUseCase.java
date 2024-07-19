package com.postgraduate.domain.mentoring.application.usecase;

import com.postgraduate.domain.mentoring.application.dto.DoneSeniorMentoringInfo;
import com.postgraduate.domain.mentoring.application.dto.ExpectedSeniorMentoringInfo;
import com.postgraduate.domain.mentoring.application.dto.WaitingSeniorMentoringInfo;
import com.postgraduate.domain.mentoring.application.dto.res.*;
import com.postgraduate.domain.mentoring.application.mapper.MentoringMapper;
import com.postgraduate.domain.mentoring.domain.entity.Mentoring;
import com.postgraduate.domain.mentoring.domain.service.MentoringGetService;
import com.postgraduate.domain.mentoring.util.DateUtils;
import com.postgraduate.domain.senior.domain.entity.Senior;
import com.postgraduate.domain.senior.domain.service.SeniorGetService;
import com.postgraduate.domain.user.user.domain.entity.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

import static com.postgraduate.domain.mentoring.util.DateUtils.stringToLocalDateTime;
import static java.time.Duration.between;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class MentoringSeniorInfoUseCase {
    private final MentoringGetService mentoringGetService;
    private final MentoringMapper mentoringMapper;
    private final SeniorGetService seniorGetService;

    public SeniorMentoringDetailResponse getSeniorMentoringDetail(User user, Long mentoringId) {
        Senior senior = seniorGetService.byUser(user);
        Mentoring mentoring = mentoringGetService.byIdAndSeniorForDetails(mentoringId, senior);
        return mentoringMapper.mapToSeniorMentoringDetail(mentoring);
    }

    public WaitingSeniorMentoringResponse getSeniorWaiting(User user) {
        Senior senior = seniorGetService.byUser(user);
        List<Mentoring> mentorings = mentoringGetService.bySeniorWaiting(senior);
        List<WaitingSeniorMentoringInfo> waitingMentoringInfos = mentorings.stream()
                .map(mentoring -> {
                    LocalDateTime expiredAt = mentoring.getCreatedAt()
                            .plusDays(2)
                            .toLocalDate()
                            .atStartOfDay();
                    LocalDateTime now = LocalDateTime.now();
                    String remain = DateUtils.remainTime(between(now, expiredAt));
                    return mentoringMapper.mapToSeniorWaitingInfo(mentoring, remain);
                })
                .toList();
        return new WaitingSeniorMentoringResponse(waitingMentoringInfos);
    }

    public ExpectedSeniorMentoringResponse getSeniorExpected(User user) {
        Senior senior = seniorGetService.byUser(user);
        List<Mentoring> mentorings = mentoringGetService.bySeniorExpected(senior);
        List<ExpectedSeniorMentoringInfo> expectedMentoringInfos = mentorings.stream()
                .map(mentoring -> {
                    LocalDateTime date = DateUtils.stringToLocalDateTime(mentoring.getDate());
                    LocalDateTime now = LocalDateTime.now();
                    long dDay = between(now, date).toDays();
                    return mentoringMapper.mapToSeniorExpectedInfo(mentoring, dDay);
                })
                .toList();
        return new ExpectedSeniorMentoringResponse(expectedMentoringInfos);
    }

    public DoneSeniorMentoringResponse getSeniorDone(User user) {
        Senior senior = seniorGetService.byUser(user);
        List<Mentoring> mentorings = mentoringGetService.bySeniorDone(senior);
        List<DoneSeniorMentoringInfo> doneSeniorMentoringInfos = mentorings.stream()
                .map(mentoring -> mentoringMapper.mapToSeniorDoneInfo(mentoring))
                .sorted((o1, o2) -> {
                    if (stringToLocalDateTime(o1.date())
                            .isAfter(stringToLocalDateTime(o2.date())))
                        return -1;
                    return 1;
                })
                .toList();
        return new DoneSeniorMentoringResponse(doneSeniorMentoringInfos);
    }
}

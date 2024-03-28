package com.postgraduate.domain.mentoring.application.usecase;

import com.postgraduate.domain.mentoring.application.dto.DoneSeniorMentoringInfo;
import com.postgraduate.domain.mentoring.application.dto.ExpectedSeniorMentoringInfo;
import com.postgraduate.domain.mentoring.application.dto.WaitingSeniorMentoringInfo;
import com.postgraduate.domain.mentoring.application.dto.res.SeniorMentoringDetailResponse;
import com.postgraduate.domain.mentoring.application.dto.res.SeniorMentoringResponse;
import com.postgraduate.domain.mentoring.application.mapper.MentoringMapper;
import com.postgraduate.domain.mentoring.domain.entity.Mentoring;
import com.postgraduate.domain.mentoring.domain.service.MentoringGetService;
import com.postgraduate.domain.mentoring.util.DateUtils;
import com.postgraduate.domain.senior.domain.entity.Senior;
import com.postgraduate.domain.senior.domain.service.SeniorGetService;
import com.postgraduate.domain.user.domain.entity.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

import static com.postgraduate.domain.mentoring.application.mapper.MentoringMapper.*;
import static java.time.Duration.between;

@Service
@Transactional
@RequiredArgsConstructor
public class MentoringSeniorInfoUseCase {
    private final MentoringGetService mentoringGetService;
    private final SeniorGetService seniorGetService;

    public SeniorMentoringDetailResponse getSeniorMentoringDetail(User user, Long mentoringId) {
        Senior senior = seniorGetService.byUser(user);
        Mentoring mentoring = mentoringGetService.byIdAndSeniorForDetails(mentoringId, senior);
        return mapToSeniorMentoringDetail(mentoring);
    }

    public SeniorMentoringResponse getSeniorWaiting(User user) {
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
                    return mapToSeniorWaitingInfo(mentoring, remain);
                })
                .toList();
        return new SeniorMentoringResponse(waitingMentoringInfos);
    }

    public SeniorMentoringResponse getSeniorExpected(User user) {
        Senior senior = seniorGetService.byUser(user);
        List<Mentoring> mentorings = mentoringGetService.bySeniorExpected(senior);
        List<ExpectedSeniorMentoringInfo> expectedMentoringInfos = mentorings.stream()
                .map(mentoring -> {
                    LocalDateTime date = DateUtils.stringToLocalDateTime(mentoring.getDate());
                    LocalDateTime now = LocalDateTime.now();
                    long dDay = between(now, date).toDays();
                    return mapToSeniorExpectedInfo(mentoring, dDay);
                })
                .toList();
        return new SeniorMentoringResponse(expectedMentoringInfos);
    }

    public SeniorMentoringResponse getSeniorDone(User user) {
        Senior senior = seniorGetService.byUser(user);
        List<Mentoring> mentorings = mentoringGetService.bySeniorDone(senior);
        List<DoneSeniorMentoringInfo> doneSeniorMentoringInfos = mentorings.stream()
                .map(MentoringMapper::mapToSeniorDoneInfo)
                .toList();
        return new SeniorMentoringResponse(doneSeniorMentoringInfos);
    }
}

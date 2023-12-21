package com.postgraduate.domain.mentoring.application.usecase;

import com.postgraduate.domain.mentoring.application.dto.DoneSeniorMentoringInfo;
import com.postgraduate.domain.mentoring.application.dto.ExpectedSeniorMentoringInfo;
import com.postgraduate.domain.mentoring.application.dto.WaitingSeniorMentoringInfo;
import com.postgraduate.domain.mentoring.application.dto.res.SeniorMentoringDetailResponse;
import com.postgraduate.domain.mentoring.application.dto.res.SeniorMentoringResponse;
import com.postgraduate.domain.mentoring.application.mapper.MentoringMapper;
import com.postgraduate.domain.mentoring.domain.entity.Mentoring;
import com.postgraduate.domain.mentoring.domain.entity.constant.Status;
import com.postgraduate.domain.mentoring.domain.service.MentoringGetService;
import com.postgraduate.domain.mentoring.exception.MentoringDoneException;
import com.postgraduate.domain.salary.domain.entity.Salary;
import com.postgraduate.domain.salary.domain.service.SalaryGetService;
import com.postgraduate.domain.senior.domain.entity.Senior;
import com.postgraduate.domain.senior.domain.service.SeniorGetService;
import com.postgraduate.domain.user.domain.entity.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;

import static com.postgraduate.domain.mentoring.application.mapper.MentoringMapper.*;
import static com.postgraduate.domain.mentoring.domain.entity.constant.Status.*;

@Service
@Transactional
@RequiredArgsConstructor
public class MentoringSeniorInfoUseCase {
    private final MentoringGetService mentoringGetService;
    private final CheckIsMyMentoringUseCase checkIsMyMentoringUseCase;
    private final SeniorGetService seniorGetService;
    private final SalaryGetService salaryGetService;

    public SeniorMentoringDetailResponse getSeniorMentoringDetail(User user, Long mentoringId) {
        Senior senior = seniorGetService.byUser(user);
        Mentoring mentoring = checkIsMyMentoringUseCase.bySenior(senior, mentoringId);
        if (mentoring.getStatus() == DONE) {
            throw new MentoringDoneException();
        }
        return mapToSeniorMentoringDetail(mentoring);
    }

    public SeniorMentoringResponse getSeniorWaiting(User user) {
        List<Mentoring> mentorings = getMentorings(user, WAITING);
        List<WaitingSeniorMentoringInfo> waitingMentoringInfos = mentorings.stream()
                .map(mentoring -> {
                    LocalDateTime expiredAt = mentoring.getCreatedAt().plusDays(2);
                    LocalDateTime now = LocalDateTime.now();
                    long remain = Duration.between(now, expiredAt).toMinutes();
                    return mapToSeniorWaitingInfo(mentoring, remain);
                })
                .toList();
        return new SeniorMentoringResponse(waitingMentoringInfos);
    }

    public SeniorMentoringResponse getSeniorExpected(User user) {
        List<Mentoring> mentorings = getMentorings(user, EXPECTED);
        List<ExpectedSeniorMentoringInfo> expectedMentoringInfos = mentorings.stream()
                .map(MentoringMapper::mapToSeniorExpectedInfo)
                .toList();
        return new SeniorMentoringResponse(expectedMentoringInfos);
    }

    public SeniorMentoringResponse getSeniorDone(User user) {
        List<Mentoring> mentorings = getMentorings(user, DONE);
        List<DoneSeniorMentoringInfo> doneMentoringInfos = mentorings.stream()
                .map(mentoring -> {
                    Salary salary = salaryGetService.byMentoring(mentoring);
                    return mapToSeniorDoneInfo(mentoring, salary);
                })
                .toList();
        return new SeniorMentoringResponse(doneMentoringInfos);
    }

    private List<Mentoring> getMentorings(User user, Status status) {
        Senior senior = seniorGetService.byUser(user);
        return mentoringGetService.mentoringBySenior(senior, status);
    }
}

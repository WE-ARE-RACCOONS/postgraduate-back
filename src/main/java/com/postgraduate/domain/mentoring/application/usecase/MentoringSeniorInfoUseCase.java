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
import com.postgraduate.domain.salary.domain.entity.Salary;
import com.postgraduate.domain.salary.domain.service.SalaryGetService;
import com.postgraduate.domain.senior.domain.entity.Senior;
import com.postgraduate.domain.senior.domain.service.SeniorGetService;
import com.postgraduate.domain.user.domain.entity.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.text.SimpleDateFormat;
import java.time.Duration;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static com.postgraduate.domain.mentoring.application.mapper.MentoringMapper.*;
import static com.postgraduate.domain.mentoring.application.mapper.MentoringMapper.mapToSeniorDoneInfo;
import static com.postgraduate.domain.mentoring.domain.entity.constant.Status.*;
import static com.postgraduate.domain.salary.util.MonthFormat.getMonthFormat;

@Service
@Transactional
@RequiredArgsConstructor
public class MentoringSeniorInfoUseCase {
    private final MentoringGetService mentoringGetService;
    private final CheckIsMyMentoringUseCase checkIsMyMentoringUseCase;
    private final SeniorGetService seniorGetService;
    private final SalaryGetService salaryGetService;

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
            LocalDateTime expiredAt = mentoring.getCreatedAt()
                    .plusDays(2)
                    .atStartOfDay();
            LocalDateTime now = LocalDateTime.now();
            long remain = Duration.between(now, expiredAt).toMinutes();
            waitingMentoringInfos.add(mapToSeniorWaitingInfo(mentoring, remain));
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
            LocalDate updatedAt = mentoring.getUpdatedAt();
            String month = updatedAt.format(getMonthFormat());
            Salary salary = salaryGetService.bySeniorAndMonth(mentoring.getSenior(), month)
                            .orElseThrow();
            doneMentoringInfos.add(mapToSeniorDoneInfo(mentoring, mentoring.getDate(), salary.getStatus()));
            //todo : 정산 관련 로직 필요, 예외 처리 필요
            /**
             * 우선 yyyy-MM 형식으로 저장
             */
        }
        return new AppliedMentoringResponse(doneMentoringInfos);
    }
}

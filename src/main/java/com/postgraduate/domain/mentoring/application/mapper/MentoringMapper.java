package com.postgraduate.domain.mentoring.application.mapper;

import com.postgraduate.domain.mentoring.application.dto.*;
import com.postgraduate.domain.mentoring.application.dto.req.MentoringApplyRequest;
import com.postgraduate.domain.mentoring.application.dto.res.AppliedMentoringDetailResponse;
import com.postgraduate.domain.mentoring.application.dto.res.SeniorMentoringDetailResponse;
import com.postgraduate.domain.mentoring.domain.entity.Mentoring;
import com.postgraduate.domain.payment.domain.entity.Payment;
import com.postgraduate.domain.salary.domain.entity.Salary;
import com.postgraduate.domain.senior.domain.entity.Info;
import com.postgraduate.domain.senior.domain.entity.Senior;
import com.postgraduate.domain.user.application.utils.UserUtils;
import com.postgraduate.domain.user.domain.entity.User;
import jakarta.validation.constraints.NotNull;

public class MentoringMapper {
    private MentoringMapper() {
        throw new IllegalArgumentException();
    }

    private static UserUtils userUtils = new UserUtils();

    public static ExpectedMentoringInfo mapToExpectedInfo(Mentoring mentoring) {
        Senior senior = mentoring.getSenior();
        User user = senior.getUser();
        Info info = senior.getInfo();
        return new ExpectedMentoringInfo(
                mentoring.getMentoringId(),
                senior.getSeniorId(),
                user.getProfile(),
                user.getNickName(),
                info.getPostgradu(),
                info.getMajor(),
                info.getLab(),
                mentoring.getDate(),
                mentoring.getTerm(),
                info.getChatLink()
        );
    }

    public static DoneMentoringInfo mapToDoneInfo(Mentoring mentoring) {
        if (mentoring.getSenior() == null || mentoring.getSenior().getUser().isDelete()) {
            return getDoneMentoringInfo(mentoring, userUtils.getArchiveSenior());
        }
        Senior senior = mentoring.getSenior();
        return getDoneMentoringInfo(mentoring, senior);
    }

    @NotNull
    private static DoneMentoringInfo getDoneMentoringInfo(Mentoring mentoring, Senior senior) {
        User user = senior.getUser();
        Info info = senior.getInfo();
        return new DoneMentoringInfo(
                mentoring.getMentoringId(),
                senior.getSeniorId(),
                user.getProfile(),
                user.getNickName(),
                info.getPostgradu(),
                info.getMajor(),
                info.getLab(),
                mentoring.getDate(),
                mentoring.getTerm()
        );
    }

    public static WaitingMentoringInfo mapToWaitingInfo(Mentoring mentoring) {
        Senior senior = mentoring.getSenior();
        User user = senior.getUser();
        Info info = senior.getInfo();
        return new WaitingMentoringInfo(
                mentoring.getMentoringId(),
                senior.getSeniorId(),
                user.getProfile(), user.getNickName(),
                info.getPostgradu(), info.getMajor(), info.getLab(),
                mentoring.getTerm());
    }
    public static AppliedMentoringDetailResponse mapToAppliedDetailInfo(Mentoring mentoring) {
        Senior senior = mentoring.getSenior();
        String[] dates = mentoring.getDate().split(",");
        return new AppliedMentoringDetailResponse(
                senior.getSeniorId(),
                senior.getUser().getProfile(),
                senior.getUser().getNickName(),
                senior.getInfo().getPostgradu(),
                senior.getInfo().getMajor(),
                senior.getInfo().getLab(),
                mentoring.getTopic(),
                mentoring.getQuestion(),
                dates
        );
    }

    public static Mentoring mapToMentoring(User user, Senior senior, Payment payment, MentoringApplyRequest request) {
        return Mentoring.builder()
                .user(user)
                .senior(senior)
                .payment(payment)
                .topic(request.topic())
                .question(request.question())
                .date(request.date())
                .build();
    }

    public static WaitingSeniorMentoringInfo mapToSeniorWaitingInfo(Mentoring mentoring, String remainTime) {
        User user = mentoring.getUser();
        return new WaitingSeniorMentoringInfo(
                mentoring.getMentoringId(),
                user.getProfile(), user.getNickName(),
                mentoring.getTerm(),
                remainTime);
    }

    public static ExpectedSeniorMentoringInfo mapToSeniorExpectedInfo(Mentoring mentoring, long dDay) {
        User user = mentoring.getUser();
        return new ExpectedSeniorMentoringInfo(mentoring.getMentoringId(),
                user.getProfile(), user.getNickName(),
                mentoring.getTerm(),
                mentoring.getDate(),
                dDay);
    }

    public static DoneSeniorMentoringInfo mapToSeniorDoneInfo(Mentoring mentoring) {
        if (mentoring.getUser() == null || mentoring.getUser().isDelete()) {
            return getDoneSeniorMentoringInfo(mentoring, userUtils.getArchiveUser());
        }
        User user = mentoring.getUser();
        return getDoneSeniorMentoringInfo(mentoring, user);
    }

    @NotNull
    private static DoneSeniorMentoringInfo getDoneSeniorMentoringInfo(Mentoring mentoring, User user) {
        Salary salary = mentoring.getSalary();
        return new DoneSeniorMentoringInfo(mentoring.getMentoringId(),
                user.getProfile(), user.getNickName(),
                mentoring.getTerm(),
                mentoring.getDate(),
                salary.getSalaryDate(), salary.status());
    }

    public static SeniorMentoringDetailResponse mapToSeniorMentoringDetail(Mentoring mentoring) {
        String[] dates = mentoring.getDate().split(",");
        User user = mentoring.getUser();
        return new SeniorMentoringDetailResponse(
                user.getProfile(), user.getNickName(),
                mentoring.getTopic(), mentoring.getQuestion(),
                dates,
                mentoring.getTerm()
        );
    }
}

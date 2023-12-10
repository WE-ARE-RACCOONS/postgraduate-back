package com.postgraduate.domain.mentoring.application.mapper;

import com.postgraduate.domain.mentoring.application.dto.*;
import com.postgraduate.domain.mentoring.application.dto.req.MentoringApplyRequest;
import com.postgraduate.domain.mentoring.application.dto.res.AppliedMentoringDetailResponse;
import com.postgraduate.domain.mentoring.application.dto.res.SeniorMentoringDetailResponse;
import com.postgraduate.domain.mentoring.domain.entity.Mentoring;
import com.postgraduate.domain.salary.domain.entity.Salary;
import com.postgraduate.domain.senior.domain.entity.Info;
import com.postgraduate.domain.senior.domain.entity.Profile;
import com.postgraduate.domain.senior.domain.entity.Senior;
import com.postgraduate.domain.user.domain.entity.User;

public class MentoringMapper {
    public static ExpectedMentoringInfo mapToExpectedInfo(Mentoring mentoring) {
        Senior senior = mentoring.getSenior();
        User user = senior.getUser();
        Info info = senior.getInfo();
        Profile profile = senior.getProfile();
        return new ExpectedMentoringInfo(
                mentoring.getMentoringId(),
                senior.getSeniorId(),
                user.getProfile(),
                user.getNickName(),
                info.getPostgradu(),
                info.getMajor(),
                info.getLab(),
                mentoring.getDate(),
                profile.getTerm(),
                profile.getChatLink()
        );
    }

    public static DoneMentoringInfo mapToDoneInfo(Mentoring mentoring) {
        Senior senior = mentoring.getSenior();
        User user = senior.getUser();
        Info info = senior.getInfo();
        Profile profile = senior.getProfile();
        return new DoneMentoringInfo(
                mentoring.getMentoringId(),
                senior.getSeniorId(),
                user.getProfile(),
                user.getNickName(),
                info.getPostgradu(),
                info.getMajor(),
                info.getLab(),
                mentoring.getDate(),
                profile.getTerm()
        );
    }

    public static WaitingMentoringInfo mapToWaitingInfo(Mentoring mentoring) {
        Senior senior = mentoring.getSenior();
        User user = senior.getUser();
        Info info = senior.getInfo();
        Profile profile = senior.getProfile();
        return new WaitingMentoringInfo(
                mentoring.getMentoringId(),
                senior.getSeniorId(),
                user.getProfile(), user.getNickName(),
                info.getPostgradu(), info.getMajor(), info.getLab(),
                profile.getTerm());
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

    public static Mentoring mapToMentoring(User user, Senior senior, MentoringApplyRequest request) {
        return Mentoring.builder()
                .user(user)
                .senior(senior)
                .topic(request.topic())
                .question(request.question())
                .date(request.date())
                .build();
    }

    public static WaitingSeniorMentoringInfo mapToSeniorWaitingInfo(Mentoring mentoring, long remainTime) {
        User user = mentoring.getUser();
        Senior senior = mentoring.getSenior();
        return new WaitingSeniorMentoringInfo(
                mentoring.getMentoringId(),
                user.getProfile(), user.getNickName(),
                senior.getProfile().getTerm(),
                remainTime);
    }

    public static ExpectedSeniorMentoringInfo mapToSeniorExpectedInfo(Mentoring mentoring) {
        User user = mentoring.getUser();
        Senior senior = mentoring.getSenior();
        return new ExpectedSeniorMentoringInfo(mentoring.getMentoringId(),
                user.getProfile(), user.getNickName(),
                senior.getProfile().getTerm(),
                mentoring.getDate());
    }

    public static DoneSeniorMentoringInfo mapToSeniorDoneInfo(Mentoring mentoring, Salary salary) {
        User user = mentoring.getUser();
        Senior senior = mentoring.getSenior();
        return new DoneSeniorMentoringInfo(mentoring.getMentoringId(),
                user.getProfile(), user.getNickName(),
                senior.getProfile().getTerm(),
                mentoring.getDate(),
                salary.getSalaryDate(), salary.getStatus());
    }

    public static SeniorMentoringDetailResponse mapToSeniorMentoringDetail(Mentoring mentoring) {
        String[] dates = mentoring.getDate().split(",");
        User user = mentoring.getUser();
        Senior senior = mentoring.getSenior();
        return new SeniorMentoringDetailResponse(
                user.getProfile(), user.getNickName(),
                mentoring.getTopic(), mentoring.getQuestion(),
                dates,
                senior.getProfile().getTerm()
        );
    }
}

package com.postgraduate.domain.mentoring.application.mapper;

import com.postgraduate.domain.mentoring.application.dto.*;
import com.postgraduate.domain.mentoring.application.dto.req.MentoringApplyRequest;
import com.postgraduate.domain.mentoring.application.dto.res.AppliedMentoringDetailResponse;
import com.postgraduate.domain.mentoring.application.dto.res.SeniorMentoringDetailResponse;
import com.postgraduate.domain.mentoring.domain.entity.Mentoring;
import com.postgraduate.domain.senior.domain.entity.Senior;
import com.postgraduate.domain.user.domain.entity.User;

public class MentoringMapper {
    public static ExpectedMentoringInfo mapToExpectedInfo(Mentoring mentoring) {
        Senior senior = mentoring.getSenior();
        return ExpectedMentoringInfo.builder()
                .mentoringId(mentoring.getMentoringId())
                .seniorId(senior.getSeniorId())
                .profile(senior.getUser().getProfile())
                .nickName(senior.getUser().getNickName())
                .postgradu(senior.getInfo().getPostgradu())
                .lab(senior.getInfo().getLab())
                .major(senior.getInfo().getMajor())
                .term(senior.getProfile().getTerm())
                .date(mentoring.getDate())
                .chatLink(senior.getProfile().getChatLink())
                .build();
    }

    public static DoneMentoringInfo mapToDoneInfo(Mentoring mentoring) {
        Senior senior = mentoring.getSenior();
        return DoneMentoringInfo.builder()
                .mentoringId(mentoring.getMentoringId())
                .seniorId(senior.getSeniorId())
                .profile(senior.getUser().getProfile())
                .nickName(senior.getUser().getNickName())
                .postgradu(senior.getInfo().getPostgradu())
                .lab(senior.getInfo().getLab())
                .major(senior.getInfo().getMajor())
                .term(senior.getProfile().getTerm())
                .date(mentoring.getDate())
                .build();
    }

    public static WaitingMentoringInfo mapToWaitingInfo(Mentoring mentoring) {
        Senior senior = mentoring.getSenior();
        return WaitingMentoringInfo.builder()
                .mentoringId(mentoring.getMentoringId())
                .seniorId(senior.getSeniorId())
                .profile(senior.getUser().getProfile())
                .nickName(senior.getUser().getNickName())
                .postgradu(senior.getInfo().getPostgradu())
                .lab(senior.getInfo().getLab())
                .major(senior.getInfo().getMajor())
                .term(senior.getProfile().getTerm())
                .build();
    }
    public static AppliedMentoringDetailResponse mapToAppliedDetailInfo(Mentoring mentoring) {
        Senior senior = mentoring.getSenior();
        String[] dates = mentoring.getDate().split(",");
        return AppliedMentoringDetailResponse.builder()
                .seniorId(senior.getSeniorId())
                .profile(senior.getUser().getProfile())
                .nickName(senior.getUser().getNickName())
                .postgradu(senior.getInfo().getPostgradu())
                .major(senior.getInfo().getMajor())
                .lab(senior.getInfo().getLab())
                .topic(mentoring.getTopic())
                .question(mentoring.getQuestion())
                .dates(dates)
                .build();
    }

    public static Mentoring mapToMentoring(User user, Senior senior, MentoringApplyRequest request) {
        return Mentoring.builder()
                .user(user)
                .senior(senior)
                .topic(request.getTopic())
                .question(request.getQuestion())
                .date(request.getDate())
                .build();
    }

    public static WaitingSeniorMentoringInfo mapToSeniorWaitingInfo(Mentoring mentoring, long remainTime) {
        return WaitingSeniorMentoringInfo.builder()
                .mentoringId(mentoring.getMentoringId())
                .profile(mentoring.getUser().getProfile())
                .nickname(mentoring.getUser().getNickName())
                .term(mentoring.getSenior().getProfile().getTerm())
                .remainTime(remainTime)
                .build();
    }

    public static ExpectedSeniorMentoringInfo mapToSeniorExpectedInfo(Mentoring mentoring) {
        return ExpectedSeniorMentoringInfo.builder()
                .mentoringId(mentoring.getMentoringId())
                .profile(mentoring.getUser().getProfile())
                .nickname(mentoring.getUser().getNickName())
                .date(mentoring.getDate())
                .term(mentoring.getSenior().getProfile().getTerm())
                .build();
    }

    public static DoneSeniorMentoringInfo mapToSeniorDoneInfo(Mentoring mentoring, String month, Boolean status) {
        return DoneSeniorMentoringInfo.builder()
                .mentoringId(mentoring.getMentoringId())
                .profile(mentoring.getUser().getProfile())
                .nickname(mentoring.getUser().getNickName())
                .date(mentoring.getDate())
                .term(mentoring.getSenior().getProfile().getTerm())
                .month(month)
                .status(status)
                .build();
    }

    public static SeniorMentoringDetailResponse mapToSeniorMentoringDetail(Mentoring mentoring) {
        String[] dates = mentoring.getDate().split(",");
        User user = mentoring.getUser();
        Senior senior = mentoring.getSenior();
        return SeniorMentoringDetailResponse.builder()
                .profile(user.getProfile())
                .nickName(user.getNickName())
                .topic(mentoring.getTopic())
                .question(mentoring.getQuestion())
                .dates(dates)
                .term(senior.getProfile().getTerm())
                .build();
    }
}

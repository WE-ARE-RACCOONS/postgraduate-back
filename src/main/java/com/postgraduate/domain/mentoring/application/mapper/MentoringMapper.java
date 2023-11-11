package com.postgraduate.domain.mentoring.application.mapper;

import com.postgraduate.domain.mentoring.application.dto.DoneMentoringInfo;
import com.postgraduate.domain.mentoring.application.dto.ExpectedMentoringInfo;
import com.postgraduate.domain.mentoring.application.dto.WaitingMentoringInfo;
import com.postgraduate.domain.mentoring.application.dto.req.MentoringApplyRequest;
import com.postgraduate.domain.mentoring.application.dto.res.AppliedMentoringDetailResponse;
import com.postgraduate.domain.mentoring.application.dto.res.SeniorMentoringDetailResponse;
import com.postgraduate.domain.mentoring.application.dto.res.SeniorMentoringResponse;
import com.postgraduate.domain.mentoring.domain.entity.Mentoring;
import com.postgraduate.domain.senior.domain.entity.Senior;
import com.postgraduate.domain.user.domain.entity.User;

import java.util.List;
import java.util.stream.Stream;

public class MentoringMapper {
    public static ExpectedMentoringInfo mapToExpectedInfo(Mentoring mentoring) {
        Senior senior = mentoring.getSenior();
        return ExpectedMentoringInfo.builder()
                .mentoringId(mentoring.getMentoringId())
                .seniorId(senior.getSeniorId())
                .nickName(senior.getUser().getNickName())
                .postgradu(senior.getInfo().getPostgradu())
                .professor(senior.getInfo().getProfessor())
                .field(senior.getInfo().getField())
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
                .nickName(senior.getUser().getNickName())
                .postgradu(senior.getInfo().getPostgradu())
                .professor(senior.getInfo().getProfessor())
                .field(senior.getInfo().getField())
                .term(senior.getProfile().getTerm())
                .date(mentoring.getDate())
                .build();
    }

    public static WaitingMentoringInfo mapToWaitingInfo(Mentoring mentoring) {
        Senior senior = mentoring.getSenior();
        return WaitingMentoringInfo.builder()
                .mentoringId(mentoring.getMentoringId())
                .term(senior.getProfile().getTerm())
                .seniorId(senior.getSeniorId())
                .nickName(senior.getUser().getNickName())
                .postgradu(senior.getInfo().getPostgradu())
                .major(senior.getInfo().getMajor())
                .field(senior.getInfo().getField())
                .build();
    }
    public static AppliedMentoringDetailResponse mapToAppliedDetailInfo(Mentoring mentoring) {
        Senior senior = mentoring.getSenior();
        List<String> dates = Stream.of(mentoring.getDate())
                .toList();
        return AppliedMentoringDetailResponse.builder()
                .seniorId(senior.getSeniorId())
                .nickName(senior.getUser().getNickName())
                .field(senior.getInfo().getField())
                .professor(senior.getInfo().getProfessor())
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

    public static SeniorMentoringResponse mapToSeniorMentoring(Mentoring mentoring) {
        String[] dates = mentoring.getDate().split(",");
        return SeniorMentoringResponse.builder()
                .mentoringId(mentoring.getMentoringId())
                .nickname(mentoring.getUser().getNickName())
                .dates(dates)
                .term(mentoring.getSenior().getProfile().getTerm())
                .build();
    }

    public static SeniorMentoringDetailResponse mapToSeniorMentoringDetail(Mentoring mentoring) {
        String[] dates = mentoring.getDate().split(",");
        return SeniorMentoringDetailResponse.builder()
                .nickName(mentoring.getUser().getNickName())
                .topic(mentoring.getTopic())
                .question(mentoring.getQuestion())
                .dates(dates)
                .build();
    }
}

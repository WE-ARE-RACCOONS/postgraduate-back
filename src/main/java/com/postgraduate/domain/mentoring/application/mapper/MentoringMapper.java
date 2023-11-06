package com.postgraduate.domain.mentoring.application.mapper;

import com.postgraduate.domain.mentoring.application.dto.AppliedMentoringInfo;
import com.postgraduate.domain.mentoring.application.dto.req.MentoringApplyRequest;
import com.postgraduate.domain.mentoring.application.dto.res.AppliedMentoringDetailResponse;
import com.postgraduate.domain.mentoring.application.dto.res.SeniorMentoringResponse;
import com.postgraduate.domain.mentoring.domain.entity.Mentoring;
import com.postgraduate.domain.senior.domain.entity.Senior;
import com.postgraduate.domain.user.domain.entity.User;

import java.util.List;
import java.util.stream.Stream;

public class MentoringMapper {
    public static AppliedMentoringInfo mapToExpectedAppliedInfo(Mentoring mentoring) {
        Senior senior = mentoring.getSenior();
        String[] dates = mentoring.getDate().split(",");
        return AppliedMentoringInfo.builder()
                .mentoringId(mentoring.getMentoringId())
                .dates(dates)
                .seniorId(senior.getSeniorId())
                .nickName(senior.getUser().getNickName())
                .postgradu(senior.getPostgradu())
                .professor(senior.getProfessor())
                .field(senior.getField())
                .chatLink(senior.getChatLink())
                .build();
    }
    public static AppliedMentoringInfo mapToWaitingOrDoneAppliedInfo(Mentoring mentoring) {
        Senior senior = mentoring.getSenior();
        String[] dates = mentoring.getDate().split(",");
        return AppliedMentoringInfo.builder()
                .mentoringId(mentoring.getMentoringId())
                .dates(dates)
                .term(senior.getTerm())
                .seniorId(senior.getSeniorId())
                .nickName(senior.getUser().getNickName())
                .postgradu(senior.getPostgradu())
                .professor(senior.getProfessor())
                .field(senior.getField())
                .build();
    }
    public static AppliedMentoringDetailResponse mapToAppliedDetailInfo(Mentoring mentoring) {
        Senior senior = mentoring.getSenior();
        List<String> dates = Stream.of(mentoring.getDate())
                .toList();
        return AppliedMentoringDetailResponse.builder()
                .seniorId(senior.getSeniorId())
                .nickName(senior.getUser().getNickName())
                .field(senior.getField())
                .professor(senior.getProfessor())
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
                .term(mentoring.getSenior().getTerm())
                .build();
    }
}

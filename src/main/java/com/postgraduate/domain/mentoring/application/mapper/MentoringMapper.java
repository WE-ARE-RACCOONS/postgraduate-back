package com.postgraduate.domain.mentoring.application.mapper;

import com.postgraduate.domain.mentoring.application.dto.AppliedMentoringInfo;
import com.postgraduate.domain.mentoring.application.dto.req.MentoringApplyRequest;
import com.postgraduate.domain.mentoring.application.dto.res.AppliedMentoringDetailResponse;
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
                .postgradu(senior.getInfo().getPostgradu())
                .professor(senior.getInfo().getProfessor())
                .field(senior.getInfo().getField())
                .chatLink(senior.getProfile().getChatLink())
                .build();
    }
    public static AppliedMentoringInfo mapToWaitingOrDoneAppliedInfo(Mentoring mentoring) {
        Senior senior = mentoring.getSenior();
        String[] dates = mentoring.getDate().split(",");
        return AppliedMentoringInfo.builder()
                .mentoringId(mentoring.getMentoringId())
                .dates(dates)
                .term(senior.getProfile().getTerm())
                .seniorId(senior.getSeniorId())
                .nickName(senior.getUser().getNickName())
                .postgradu(senior.getInfo().getPostgradu())
                .professor(senior.getInfo().getProfessor())
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
}

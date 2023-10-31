package com.postgraduate.domain.mentoring.application.mapper;

import com.postgraduate.domain.mentoring.application.dto.AppliedMentoringDetailResponse;
import com.postgraduate.domain.mentoring.application.dto.AppliedMentoringInfo;
import com.postgraduate.domain.mentoring.domain.entity.Mentoring;
import com.postgraduate.domain.senior.domain.entity.Senior;

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
                .lab(senior.getLab())
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
                .lab(senior.getLab())
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
                .lab(senior.getLab())
                .professor(senior.getProfessor())
                .topic(mentoring.getTopic())
                .question(mentoring.getQuestion())
                .dates(dates)
                .build();
    }
}

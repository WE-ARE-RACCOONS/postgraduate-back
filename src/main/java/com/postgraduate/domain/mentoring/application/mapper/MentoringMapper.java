package com.postgraduate.domain.mentoring.application.mapper;

import com.postgraduate.domain.mentoring.application.dto.AppliedMentoringInfo;
import com.postgraduate.domain.mentoring.domain.entity.Mentoring;
import com.postgraduate.domain.senior.domain.entity.Senior;

import java.util.List;
import java.util.stream.Stream;

public class MentoringMapper {
    public static AppliedMentoringInfo mapToExpectedInfo(Mentoring mentoring) {
        Senior senior = mentoring.getSenior();
        List<String> dates = Stream.of(mentoring.getDate())
                .toList();
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
    public static AppliedMentoringInfo mapToWaitingOrDoneMentoringInfo(Mentoring mentoring) {
        Senior senior = mentoring.getSenior();
        List<String> dates = Stream.of(mentoring.getDate())
                .toList();
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
}

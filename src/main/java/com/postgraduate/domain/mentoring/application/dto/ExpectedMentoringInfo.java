package com.postgraduate.domain.mentoring.application.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;


@Getter
@Builder
@AllArgsConstructor
public class ExpectedMentoringInfo {
    private Long mentoringId;
    private Long seniorId;
    private String profile;
    private String nickName;
    private String postgradu;
    private String major;
    private String lab;
    private String date;
    private int term;
    private String chatLink;
}
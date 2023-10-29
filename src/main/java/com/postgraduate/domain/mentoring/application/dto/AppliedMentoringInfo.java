package com.postgraduate.domain.mentoring.application.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Getter
@Builder
@AllArgsConstructor
public class AppliedMentoringInfo {
    Long mentoringId;
    Long seniorId;
    String nickName;
    String postgradu;
    String field;
    String lab;
    String professor;
    List<String> dates;
    int term;
    String chatLink;
}
package com.postgraduate.domain.mentoring.application.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;


@Getter
@Builder
@AllArgsConstructor
public class ExpectedMentoringInfo {
    Long mentoringId;
    Long seniorId;
    String nickName;
    String postgradu;
    String field;
    String professor;
    String date;
    int term;
    String chatLink;
}
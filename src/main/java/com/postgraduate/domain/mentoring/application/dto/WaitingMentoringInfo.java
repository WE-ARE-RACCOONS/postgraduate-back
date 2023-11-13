package com.postgraduate.domain.mentoring.application.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;


@Getter
@Builder
@AllArgsConstructor
public class WaitingMentoringInfo {
    Long mentoringId;
    Long seniorId;
    String nickName;
    String postgradu;
    String field;
    String major;
    int term;
}
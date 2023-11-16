package com.postgraduate.domain.mentoring.application.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@AllArgsConstructor
public class ExpectedSeniorMentoringInfo {
    private Long mentoringId;
    private String profile;
    private String nickname;
    private int term;
    private String date;
}

package com.postgraduate.domain.mentoring.application.dto.res;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Builder
@AllArgsConstructor
@Getter
public class SeniorMentoringDetailResponse {
    private String profile;
    private String nickName;
    private String topic;
    private String question;
    private String[] dates;
    private int term;
}

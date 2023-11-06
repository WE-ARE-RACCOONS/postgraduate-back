package com.postgraduate.domain.mentoring.application.dto.res;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Builder
@AllArgsConstructor
@Getter
public class SeniorMentoringResponse {
    private String nickname;
    private String[] dates;
    private int term;
}

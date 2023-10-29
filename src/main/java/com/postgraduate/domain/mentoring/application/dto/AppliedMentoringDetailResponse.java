package com.postgraduate.domain.mentoring.application.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Getter
@Builder
@AllArgsConstructor
public class AppliedMentoringDetailResponse {
        Long seniorId;
        String nickName;
        String field;
        String lab;
        String professor;
        String topic;
        String question;
        List<String> dates;
}

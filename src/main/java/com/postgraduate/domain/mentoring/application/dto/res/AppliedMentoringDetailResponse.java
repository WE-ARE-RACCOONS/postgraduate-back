package com.postgraduate.domain.mentoring.application.dto.res;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@AllArgsConstructor
public class AppliedMentoringDetailResponse {
        private Long seniorId;
        private String profile;
        private String nickName;
        private String postgradu;
        private String major;
        private String lab;
        private int term;
        private String topic;
        private String question;
        private String[] dates;
}

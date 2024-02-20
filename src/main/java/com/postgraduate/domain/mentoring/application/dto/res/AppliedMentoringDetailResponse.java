package com.postgraduate.domain.mentoring.application.dto.res;

public record AppliedMentoringDetailResponse(
        Long seniorId,
        String profile,
        String nickName,
        String postgradu,
        String major,
        String lab,
        String topic,
        String question,
        String[] dates
) {
}

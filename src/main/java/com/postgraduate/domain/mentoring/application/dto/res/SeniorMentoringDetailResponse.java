package com.postgraduate.domain.mentoring.application.dto.res;

public record SeniorMentoringDetailResponse(String profile, String nickName, String topic, String question,
                                            String[] dates, int term) {
}

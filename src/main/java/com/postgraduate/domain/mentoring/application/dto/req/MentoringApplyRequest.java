package com.postgraduate.domain.mentoring.application.dto.req;

public record MentoringApplyRequest(
        String orderId,
        String topic,
        String question,
        String date
) {
}

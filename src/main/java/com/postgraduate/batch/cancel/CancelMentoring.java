package com.postgraduate.batch.cancel;

public record CancelMentoring(
    Long mentoringId,
    Long userId,
    Long seniorId,
    Long paymentId
) {
}

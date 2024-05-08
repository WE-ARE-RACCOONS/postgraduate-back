package com.postgraduate.global.batch.done;

public record DoneMentoring(
        Long mentoringId,
        Long seniorId,
        Long salaryId,
        String date,
        int pay
) {
}

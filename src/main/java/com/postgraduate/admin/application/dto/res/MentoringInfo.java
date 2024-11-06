package com.postgraduate.admin.application.dto.res;

import com.postgraduate.domain.mentoring.domain.entity.constant.MentoringStatus;

import java.time.LocalDateTime;

public record MentoringInfo(
        Long mentoringId,
        MentoringStatus mentoringStatus,
        String nickName,
        String phoneNumber,
        LocalDateTime createdAt,
        String date
) {
}

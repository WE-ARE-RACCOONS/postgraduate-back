package com.postgraduate.domain.admin.application.dto;

import com.postgraduate.domain.mentoring.domain.entity.constant.Status;

import java.time.LocalDateTime;

public record MentoringInfo(
        Long mentoringId,
        Status status,
        String nickName,
        String phoneNumber,
        LocalDateTime createdAt
) {
}

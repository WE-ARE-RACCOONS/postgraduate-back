package com.postgraduate.domain.admin.application.dto;

import com.postgraduate.domain.mentoring.domain.entity.constant.Status;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDateTime;

public record MentoringInfo(
        @NotNull
        Long mentoringId,
        @NotNull
        Status status,
        @NotNull
        String nickName,
        @NotNull
        String phoneNumber,
        @NotNull
        LocalDateTime createdAt
) {
}

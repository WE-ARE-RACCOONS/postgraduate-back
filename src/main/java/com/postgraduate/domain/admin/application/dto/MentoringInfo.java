package com.postgraduate.domain.admin.application.dto;

import com.postgraduate.domain.mentoring.domain.entity.constant.Status;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDate;

public record MentoringInfo(
        @NotNull
        Long mentoringId,
        @NotNull
        Status status,
        @NotNull
        String userNickName,
        @NotNull
        String userPhoneNumber,
        @NotNull
        String seniorNickName,
        @NotNull
        String seniorPhoneNumber,
        @NotNull
        LocalDate createdAt
) {
}

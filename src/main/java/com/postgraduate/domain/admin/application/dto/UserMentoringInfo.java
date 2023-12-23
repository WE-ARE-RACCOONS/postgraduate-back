package com.postgraduate.domain.admin.application.dto;

import jakarta.validation.constraints.NotNull;

public record UserMentoringInfo(
        @NotNull
        String nickName,
        @NotNull
        String phoneNumber
) {
}

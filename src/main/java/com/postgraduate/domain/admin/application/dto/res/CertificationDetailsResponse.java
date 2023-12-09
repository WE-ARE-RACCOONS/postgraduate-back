package com.postgraduate.domain.admin.application.dto.res;

import jakarta.validation.constraints.NotNull;

import java.time.LocalDate;

public record CertificationDetailsResponse(
        @NotNull
        String certification,
        @NotNull
        String nickName,
        @NotNull
        String phoneNumber,
        @NotNull
        LocalDate createdAt,
        @NotNull
        String postgradu,
        @NotNull
        String major,
        @NotNull
        String field,
        @NotNull
        String lab,
        @NotNull
        String professor,
        @NotNull
        String keyword
) { }

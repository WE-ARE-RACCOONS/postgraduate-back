package com.postgraduate.domain.admin.application.dto.res;

import jakarta.validation.constraints.NotNull;

public record CertificationDetailsResponse(
        @NotNull
        String certification,
        @NotNull
        String nickName,
        @NotNull
        String phoneNumber,
        //인증 신청 날짜
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

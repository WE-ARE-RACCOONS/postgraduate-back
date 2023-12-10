package com.postgraduate.domain.auth.application.dto.req;

import jakarta.validation.constraints.NotNull;

public record SignUpRequest(
        @NotNull
        Long socialId,
        @NotNull
        String phoneNumber,
        @NotNull
        String nickName,
        @NotNull
        Boolean marketingReceive,
        String major,
        String field,
        @NotNull
        Boolean matchingReceive
) { }

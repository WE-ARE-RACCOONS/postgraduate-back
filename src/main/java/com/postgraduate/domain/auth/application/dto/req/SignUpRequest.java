package com.postgraduate.domain.auth.application.dto.req;

import jakarta.validation.constraints.NotBlank;

public record SignUpRequest(
        Long socialId,
        @NotBlank
        String phoneNumber,
        @NotBlank
        String nickName,
        Boolean marketingReceive,
        String major,
        String field,
        Boolean matchingReceive
) { }

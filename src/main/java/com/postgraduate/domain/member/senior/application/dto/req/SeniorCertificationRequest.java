package com.postgraduate.domain.member.senior.application.dto.req;

import jakarta.validation.constraints.NotBlank;

public record SeniorCertificationRequest(
        @NotBlank
        String certification
) { }

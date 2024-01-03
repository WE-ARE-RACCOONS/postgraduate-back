package com.postgraduate.domain.senior.application.dto.req;

import jakarta.validation.constraints.NotBlank;

public record SeniorMyPageUserAccountRequest(
        @NotBlank String nickName,
        @NotBlank String phoneNumber,
        @NotBlank String profile,
        String accountNumber,
        String bank,
        String accountHolder
) {}

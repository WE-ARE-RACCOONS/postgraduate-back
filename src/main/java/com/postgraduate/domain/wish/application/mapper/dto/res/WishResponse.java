package com.postgraduate.domain.wish.application.mapper.dto.res;

import jakarta.validation.constraints.NotNull;

import java.time.LocalDate;

public record WishResponse(
        @NotNull
        String nickName,
        @NotNull
        String phoneNumber,
        @NotNull
        LocalDate createAt,
        @NotNull
        Boolean marketingReceive,
        @NotNull
        Boolean matchingReceive,
        @NotNull
        String major,
        @NotNull
        String field
) {}

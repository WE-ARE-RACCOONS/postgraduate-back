package com.postgraduate.domain.wish.application.mapper.dto.res;

import jakarta.validation.constraints.NotNull;

import java.time.LocalDateTime;

public record WishResponse(
        @NotNull
        String nickName,
        @NotNull
        String phoneNumber,
        @NotNull
        LocalDateTime createAt,
        @NotNull
        Boolean marketingReceive,
        @NotNull
        Boolean matchingReceive,
        String major,
        String field
) {
}

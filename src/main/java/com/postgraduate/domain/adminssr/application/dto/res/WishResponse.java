package com.postgraduate.domain.adminssr.application.dto.res;

import java.time.LocalDateTime;

public record WishResponse(
        Long wishId,
        String nickName,
        String phoneNumber,
        LocalDateTime createAt,
        Boolean marketingReceive,
        Boolean matchingReceive,
        String major,
        String field
) {
}

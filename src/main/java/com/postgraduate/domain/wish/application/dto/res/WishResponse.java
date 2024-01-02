package com.postgraduate.domain.wish.application.dto.res;

import java.time.LocalDateTime;

public record WishResponse(
        String nickName,
        String phoneNumber,
        LocalDateTime createAt,
        Boolean marketingReceive,
        Boolean matchingReceive,
        String major,
        String field
) {}

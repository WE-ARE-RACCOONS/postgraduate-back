package com.postgraduate.admin.application.dto.res;

import java.time.LocalDateTime;

public record WaitingWishResponse(
        Long wishId,
        String field,
        String postgradu,
        String professor,
        String lab,
        String phoneNumber,
        LocalDateTime createAt
) {
}

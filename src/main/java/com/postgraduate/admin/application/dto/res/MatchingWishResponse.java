package com.postgraduate.admin.application.dto.res;


import java.time.LocalDateTime;

public record MatchingWishResponse(
        Long wishId,
        String field,
        String postgradu,
        String professor,
        String lab,
        String phoneNumber,
        LocalDateTime createAt,
        LocalDateTime updatedAt
) {
}

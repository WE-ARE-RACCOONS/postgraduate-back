package com.postgraduate.domain.adminssr.application.dto.res;

import java.time.LocalDateTime;

public record CertificationDetailsResponse(
        Long seniorId,
        String certification,
        String nickName,
        String phoneNumber,
        LocalDateTime createdAt,
        String postgradu,
        String major,
        String field,
        String lab,
        String professor,
        String keyword
) { }

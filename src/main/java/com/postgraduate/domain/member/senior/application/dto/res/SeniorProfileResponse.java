package com.postgraduate.domain.member.senior.application.dto.res;

public record SeniorProfileResponse(
        String nickName,
        String profile,
        String postgradu,
        String major,
        String lab,
        Integer term,
        Long userId,
        String phoneNumber
) {}

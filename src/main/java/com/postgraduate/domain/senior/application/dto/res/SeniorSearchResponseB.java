package com.postgraduate.domain.senior.application.dto.res;

public record SeniorSearchResponseB(
        Long seniorId,
        boolean certification,
        String profile,
        String nickName,
        String postgradu,
        String major,
        String lab,
        String professor,
        String[] keyword
) {}

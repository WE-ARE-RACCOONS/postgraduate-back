package com.postgraduate.domain.member.senior.application.dto.res;

import java.util.List;

public record SeniorDetailResponse(
        boolean isMine,
        boolean certification,
        String nickName,
        int term,
        String profile,
        String postgradu,
        String major,
        String lab,
        String professor,
        String[] keyword,
        String info,
        String oneLiner,
        String target,
        List<AvailableTimeResponse> times
) { }

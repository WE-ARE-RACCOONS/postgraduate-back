package com.postgraduate.domain.member.senior.application.dto.res;

import java.util.List;

public record AvailableTimesResponse(
        String nickName,
        List<AvailableTimeResponse> times
) {}

package com.postgraduate.domain.senior.available.application.dto.res;

import java.util.List;

public record AvailableTimesResponse(
        String nickName,
        List<AvailableTimeResponse> times
) {}

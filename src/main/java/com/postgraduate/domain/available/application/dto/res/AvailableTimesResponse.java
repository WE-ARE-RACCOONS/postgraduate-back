package com.postgraduate.domain.available.application.dto.res;

import java.util.List;

public record AvailableTimesResponse(
        String nickName,
        List<AvailableTimeResponse> times
) {
}

package com.postgraduate.domain.available.application.mapper;

import com.postgraduate.domain.available.application.dto.req.AvailableCreateRequest;
import com.postgraduate.domain.available.application.dto.res.AvailableTimeResponse;
import com.postgraduate.domain.available.domain.entity.Available;
import com.postgraduate.domain.senior.domain.entity.Senior;

public class AvailableMapper {
    public static Available mapToAvailable(Senior senior, AvailableCreateRequest createRequest) {
        return Available.builder()
                .senior(senior)
                .day(createRequest.day())
                .startTime(createRequest.startTime())
                .endTime(createRequest.endTime())
                .build();
    }

    public static AvailableTimeResponse mapToAvailableTimes(Available available) {
        return new AvailableTimeResponse(available.getDay(), available.getStartTime(), available.getEndTime());
    }
}

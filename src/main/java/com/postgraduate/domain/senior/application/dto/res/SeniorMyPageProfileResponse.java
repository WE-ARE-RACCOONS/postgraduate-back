package com.postgraduate.domain.senior.application.dto.res;


import com.postgraduate.domain.available.application.dto.res.AvailableTimeResponse;

import java.util.List;

public record SeniorMyPageProfileResponse(
        String lab,
        String[] keyword,
        String info,
        String target,
        String chatLink,
        String[] field,
        String oneLiner,
        List<AvailableTimeResponse> times
) { }

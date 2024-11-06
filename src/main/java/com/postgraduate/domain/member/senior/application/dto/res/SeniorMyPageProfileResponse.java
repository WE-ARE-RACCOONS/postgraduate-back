package com.postgraduate.domain.member.senior.application.dto.res;


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

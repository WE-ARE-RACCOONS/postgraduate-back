package com.postgraduate.domain.member.senior.application.dto.res;

import java.util.List;

public record AllSeniorSearchResponse(
        List<SeniorSearchResponse> seniorSearchResponses,
        Long totalElements
) {}

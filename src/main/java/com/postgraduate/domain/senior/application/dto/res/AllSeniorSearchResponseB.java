package com.postgraduate.domain.senior.application.dto.res;

import java.util.List;

public record AllSeniorSearchResponseB(
        List<SeniorSearchResponseB> seniorSearchResponses,
        Long totalElements
) {}

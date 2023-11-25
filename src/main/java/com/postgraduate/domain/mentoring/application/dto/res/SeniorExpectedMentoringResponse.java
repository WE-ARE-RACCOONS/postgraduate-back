package com.postgraduate.domain.mentoring.application.dto.res;

import com.postgraduate.domain.mentoring.application.dto.ExpectedSeniorMentoringInfo;

import java.util.List;

public record SeniorExpectedMentoringResponse(List<ExpectedSeniorMentoringInfo> expectedSeniorMentoringInfos) {
}

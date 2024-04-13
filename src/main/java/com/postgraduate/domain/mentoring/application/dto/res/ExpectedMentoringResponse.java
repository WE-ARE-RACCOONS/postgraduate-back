package com.postgraduate.domain.mentoring.application.dto.res;

import com.postgraduate.domain.mentoring.application.dto.ExpectedMentoringInfo;

import java.util.List;

public record ExpectedMentoringResponse(List<ExpectedMentoringInfo> mentoringInfos) {
}

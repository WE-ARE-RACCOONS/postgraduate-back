package com.postgraduate.domain.mentoring.application.dto.res;

import com.postgraduate.domain.mentoring.application.dto.WaitingSeniorMentoringInfo;

import java.util.List;

public record WaitingSeniorMentoringResponse(List<WaitingSeniorMentoringInfo> seniorMentoringInfos) {
}

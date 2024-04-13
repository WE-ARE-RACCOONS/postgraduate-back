package com.postgraduate.domain.mentoring.application.dto.res;

import com.postgraduate.domain.mentoring.application.dto.WaitingMentoringInfo;

import java.util.List;

public record WaitingMentoringResponse(List<WaitingMentoringInfo> mentoringInfos) {
}

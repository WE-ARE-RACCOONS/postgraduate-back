package com.postgraduate.domain.mentoring.application.dto.res;

import com.postgraduate.domain.mentoring.application.dto.WaitingMentoringInfo;

import java.util.List;

public record AppliedWaitingMentoringResponse(List<WaitingMentoringInfo> waitingMentoringInfos) {
}

package com.postgraduate.domain.mentoring.application.dto.res;

import com.postgraduate.domain.mentoring.application.dto.DoneMentoringInfo;

import java.util.List;

public record AppliedDoneMentoringResponse(List<DoneMentoringInfo> doneMentoringInfos) {
}

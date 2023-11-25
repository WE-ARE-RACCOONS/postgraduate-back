package com.postgraduate.domain.mentoring.application.dto.res;

import com.postgraduate.domain.mentoring.application.dto.DoneSeniorMentoringInfo;

import java.util.List;

public record SeniorDoneMentoringResponse(List<DoneSeniorMentoringInfo> doneSeniorMentoringInfos) {
}

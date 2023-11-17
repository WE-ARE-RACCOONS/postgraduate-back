package com.postgraduate.domain.mentoring.application.dto.res;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Getter
@Builder
@AllArgsConstructor
public class AppliedMentoringResponse {
    private List<?> appliedMentoringInfos;
}

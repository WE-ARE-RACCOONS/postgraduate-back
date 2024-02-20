package com.postgraduate.domain.admin.application.dto.res;

import com.postgraduate.domain.admin.application.dto.SeniorInfo;

import java.util.List;

public record SeniorManageResponse(
        List<SeniorInfo> seniorInfo,
        Long totalElements,
        Integer totalPages
) {
}

package com.postgraduate.domain.admin.application.dto.res;

import com.postgraduate.domain.admin.application.dto.SeniorInfo;
import jakarta.validation.constraints.NotNull;

import java.util.List;

public record SeniorManageResponse(
        @NotNull
        List<SeniorInfo> seniorInfo,
        @NotNull
        Long count
) {
}

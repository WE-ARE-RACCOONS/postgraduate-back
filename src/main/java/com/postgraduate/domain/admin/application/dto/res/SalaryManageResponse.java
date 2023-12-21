package com.postgraduate.domain.admin.application.dto.res;

import com.postgraduate.domain.admin.application.dto.SalaryInfo;
import jakarta.validation.constraints.NotNull;

import java.util.List;

public record SalaryManageResponse(
        @NotNull
        List<SalaryInfo> salaryInfos,
        @NotNull
        Long totalElements,
        @NotNull
        Integer totalPages
) {
}

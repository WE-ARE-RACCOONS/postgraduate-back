package com.postgraduate.domain.admin.application.dto.res;

import com.postgraduate.domain.admin.application.dto.SalaryInfo;

import java.util.List;

public record SalaryManageResponse(
        List<SalaryInfo> salaryInfos,
        Long totalElements,
        Integer totalPages
) {
}

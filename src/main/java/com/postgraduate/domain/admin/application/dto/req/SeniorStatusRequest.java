package com.postgraduate.domain.admin.application.dto.req;

import com.postgraduate.domain.senior.domain.entity.constant.Status;
import jakarta.validation.constraints.NotNull;

public record SeniorStatusRequest(
        @NotNull
        Status certificationStatus
) { }

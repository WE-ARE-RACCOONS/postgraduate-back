package com.postgraduate.domain.admin.application.dto.req;

import com.postgraduate.domain.senior.domain.entity.constant.Status;

public record SeniorStatusRequest(
        Status certificationStatus
) { }

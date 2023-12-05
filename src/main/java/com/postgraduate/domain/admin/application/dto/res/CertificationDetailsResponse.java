package com.postgraduate.domain.admin.application.dto.res;

import com.postgraduate.domain.admin.application.dto.CertificationInfo;
import jakarta.validation.constraints.NotNull;

public record CertificationDetailsResponse(@NotNull CertificationInfo certificationInfo) {
}

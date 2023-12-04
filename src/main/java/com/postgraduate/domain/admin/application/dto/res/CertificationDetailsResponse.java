package com.postgraduate.domain.admin.application.dto.res;

import com.postgraduate.domain.admin.application.dto.CertificationInfo;
import jakarta.annotation.Nullable;

public record CertificationDetailsResponse(@Nullable CertificationInfo certificationInfo) {
}

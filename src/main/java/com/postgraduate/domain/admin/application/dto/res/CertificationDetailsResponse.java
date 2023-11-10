package com.postgraduate.domain.admin.application.dto.res;

import com.postgraduate.domain.admin.application.dto.CertificationInfo;
import com.postgraduate.domain.admin.application.dto.CertificationProfile;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
@AllArgsConstructor
public class CertificationDetailsResponse {
    private CertificationInfo certificationInfo;
    private CertificationProfile certificationProfile;
}

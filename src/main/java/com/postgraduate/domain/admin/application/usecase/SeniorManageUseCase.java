package com.postgraduate.domain.admin.application.usecase;

import com.postgraduate.domain.admin.application.dto.CertificationInfo;
import com.postgraduate.domain.admin.application.dto.CertificationProfile;
import com.postgraduate.domain.admin.application.dto.res.CertificationDetailsResponse;
import com.postgraduate.domain.admin.application.dto.res.CertificationResponse;
import com.postgraduate.domain.admin.application.mapper.AdminMapper;
import com.postgraduate.domain.senior.domain.entity.Senior;
import com.postgraduate.domain.senior.domain.entity.constant.Status;
import com.postgraduate.domain.senior.domain.service.SeniorGetService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
public class SeniorManageUseCase {
    private final SeniorGetService seniorGetService;

    public CertificationDetailsResponse getCertificationDetails(Long seniorId) {
        Senior senior = seniorGetService.bySeniorId(seniorId);
        CertificationProfile certificationProfile = null;
        if (senior.getProfile() != null) {
            certificationProfile = AdminMapper.mapToCertificationProfile(senior);
        }
        CertificationInfo certificationInfo = AdminMapper.mapToCertificationInfo(senior);
        return new CertificationDetailsResponse(certificationInfo, certificationProfile);
    }

    public List<CertificationResponse> getCertifications() {
        List<Senior> seniors = seniorGetService.byStatus(Status.WAITING);
        return seniors.stream().map(AdminMapper::mapToCertification).toList();
    }
}

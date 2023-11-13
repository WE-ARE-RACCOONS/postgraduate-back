package com.postgraduate.domain.admin.application.usecase;

import com.postgraduate.domain.admin.application.dto.CertificationInfo;
import com.postgraduate.domain.admin.application.dto.CertificationProfile;
import com.postgraduate.domain.admin.application.dto.req.SeniorStatusRequest;
import com.postgraduate.domain.admin.application.dto.res.CertificationDetailsResponse;
import com.postgraduate.domain.admin.application.dto.res.CertificationResponse;
import com.postgraduate.domain.admin.application.dto.res.SeniorResponse;
import com.postgraduate.domain.admin.application.mapper.AdminMapper;
import com.postgraduate.domain.admin.exception.SeniorNotWaitingException;
import com.postgraduate.domain.senior.domain.entity.Senior;
import com.postgraduate.domain.senior.domain.entity.constant.Status;
import com.postgraduate.domain.senior.domain.service.SeniorGetService;
import com.postgraduate.domain.user.domain.entity.User;
import com.postgraduate.domain.user.domain.entity.constant.Role;
import com.postgraduate.domain.user.domain.service.UserUpdateService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
public class SeniorManageByAdminUseCase {
    private final SeniorGetService seniorGetService;
    private final UserUpdateService userUpdateService;

    public CertificationDetailsResponse getCertificationDetails(Long seniorId) {
        Senior senior = seniorGetService.bySeniorId(seniorId);
        if (senior.getStatus() != Status.WAITING) {
            throw new SeniorNotWaitingException();
        }
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

    public void updateSeniorStatus(Long seniorId, SeniorStatusRequest request) {
        Senior senior = seniorGetService.bySeniorId(seniorId);
        senior.updateStatus(request.getStatus());
        if (request.getStatus() == Status.APPROVE) {
            User user = senior.getUser();
            userUpdateService.updateRole(user.getUserId(), Role.SENIOR);
        }
    }

    public List<SeniorResponse> getSeniors() {
        List<Senior> seniors = seniorGetService.byStatus(Status.APPROVE);
        return seniors.stream().map(AdminMapper::mapToSeniorResponse).toList();
    }
}

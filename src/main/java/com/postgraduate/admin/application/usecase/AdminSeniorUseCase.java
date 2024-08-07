package com.postgraduate.admin.application.usecase;

import com.postgraduate.admin.application.dto.res.CertificationDetailsResponse;
import com.postgraduate.admin.application.dto.res.SeniorInfo;
import com.postgraduate.admin.application.dto.res.SeniorInfoQuery;
import com.postgraduate.admin.application.mapper.AdminMapper;
import com.postgraduate.admin.domain.service.AdminSeniorService;
import com.postgraduate.domain.senior.domain.entity.Senior;
import com.postgraduate.domain.senior.exception.SeniorCertificationException;
import com.postgraduate.global.bizppurio.application.usecase.BizppurioSeniorMessage;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static com.postgraduate.domain.senior.domain.entity.constant.Status.WAITING;

@Service
@Transactional
@RequiredArgsConstructor
public class AdminSeniorUseCase {
    private final BizppurioSeniorMessage bizppurioSeniorMessage;
    private final AdminSeniorService adminSeniorService;
    private final AdminMapper adminMapper;

    @Transactional(readOnly = true)
    public List<SeniorInfo> allSenior() {
        List<SeniorInfoQuery> salaries = adminSeniorService.allSeniors();
        return salaries.stream()
                .map(adminMapper::mapToSeniorInfo)
                .toList();
    }

    @Transactional(readOnly = true)
    public CertificationDetailsResponse getCertification(Long seniorId) {
        Senior senior = adminSeniorService.bySeniorId(seniorId);
        if (!senior.getStatus().equals(WAITING))
            throw new SeniorCertificationException();
        return adminMapper.mapToCertificationInfo(senior);
    }


    public void updateNotApprove(Long seniorId) {
        Senior senior = adminSeniorService.certificationUpdateNotApprove(seniorId);
        bizppurioSeniorMessage.certificationDenied(senior.getUser());
    }

    public void updateApprove(Long seniorId) {
        Senior senior = adminSeniorService.certificationUpdateApprove(seniorId);
        bizppurioSeniorMessage.certificationApprove(senior.getUser());
    }

}

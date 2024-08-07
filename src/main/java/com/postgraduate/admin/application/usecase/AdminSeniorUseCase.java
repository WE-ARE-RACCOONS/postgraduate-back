package com.postgraduate.admin.application.usecase;

import com.postgraduate.admin.application.dto.res.CertificationDetailsResponse;
import com.postgraduate.admin.application.dto.res.SeniorInfo;
import com.postgraduate.admin.application.mapper.AdminMapper;
import com.postgraduate.domain.salary.domain.entity.Salary;
import com.postgraduate.domain.salary.domain.service.SalaryGetService;
import com.postgraduate.domain.senior.domain.entity.Senior;
import com.postgraduate.domain.senior.domain.service.SeniorGetService;
import com.postgraduate.domain.senior.domain.service.SeniorUpdateService;
import com.postgraduate.domain.senior.exception.SeniorCertificationException;
import com.postgraduate.domain.wish.domain.entity.Wish;
import com.postgraduate.domain.wish.domain.service.WishGetService;
import com.postgraduate.global.bizppurio.application.usecase.BizppurioSeniorMessage;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

import static com.postgraduate.domain.senior.domain.entity.constant.Status.APPROVE;

@Service
@Transactional
@RequiredArgsConstructor
public class AdminSeniorUseCase {
    private final SeniorGetService seniorGetService;
    private final SeniorUpdateService seniorUpdateService;
    private final SalaryGetService salaryGetService;
    private final WishGetService wishGetService;
    private final BizppurioSeniorMessage bizppurioSeniorMessage;
    private final AdminMapper adminMapper;

    @Transactional(readOnly = true)
    public List<SeniorInfo> allSenior() {
        List<Senior> seniors = seniorGetService.allSenior();
        return seniors.stream()
                .map(senior -> {
                    Salary salary = salaryGetService.bySenior(senior);
                    Optional<Wish> wish = wishGetService.byUser(senior.getUser());
                    return adminMapper.mapToSeniorInfo(senior, salary.getTotalAmount(), wish.isPresent());
                })
                .toList();
    }

    @Transactional(readOnly = true)
    public CertificationDetailsResponse getCertification(Long seniorId) {
        Senior senior = seniorGetService.bySeniorId(seniorId);
        if (senior.getStatus() == APPROVE)
            throw new SeniorCertificationException();
        return adminMapper.mapToCertificationInfo(senior);
    }


    public void updateNotApprove(Long seniorId) {
        Senior senior = seniorGetService.bySeniorId(seniorId);
        seniorUpdateService.certificationUpdateNotApprove(senior);
        bizppurioSeniorMessage.certificationDenied(senior.getUser());
    }

    public void updateApprove(Long seniorId) {
        Senior senior = seniorGetService.bySeniorId(seniorId);
        seniorUpdateService.certificationUpdateApprove(senior);
        bizppurioSeniorMessage.certificationApprove(senior.getUser());
    }

}

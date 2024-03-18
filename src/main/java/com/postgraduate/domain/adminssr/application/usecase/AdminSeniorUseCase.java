package com.postgraduate.domain.adminssr.application.usecase;

import com.postgraduate.domain.admin.application.dto.SeniorInfo;
import com.postgraduate.domain.admin.application.dto.res.CertificationDetailsResponse;
import com.postgraduate.domain.admin.presentation.constant.SalaryStatus;
import com.postgraduate.domain.salary.domain.entity.Salary;
import com.postgraduate.domain.salary.domain.service.SalaryGetService;
import com.postgraduate.domain.senior.domain.entity.Senior;
import com.postgraduate.domain.senior.domain.service.SeniorGetService;
import com.postgraduate.domain.senior.domain.service.SeniorUpdateService;
import com.postgraduate.domain.senior.exception.SeniorCertificationException;
import com.postgraduate.domain.wish.domain.entity.Wish;
import com.postgraduate.domain.wish.domain.service.WishGetService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

import static com.postgraduate.domain.admin.application.mapper.AdminMapper.mapToCertificationInfo;
import static com.postgraduate.domain.admin.application.mapper.AdminMapper.mapToSeniorInfo;
import static com.postgraduate.domain.salary.util.SalaryUtil.getStatus;
import static com.postgraduate.domain.senior.domain.entity.constant.Status.APPROVE;

@Service
@Transactional
@RequiredArgsConstructor
public class AdminSeniorUseCase {
    private final SeniorGetService seniorGetService;
    private final SeniorUpdateService seniorUpdateService;
    private final SalaryGetService salaryGetService;
    private final WishGetService wishGetService;

    public List<SeniorInfo> allSenior() {
        List<Senior> seniors = seniorGetService.allSeniorId();
        return seniors.stream()
                .map(senior -> {
                    Salary salary = salaryGetService.bySeniorLastWeek(senior);
                    SalaryStatus salaryStatus = getStatus(salary);
                    Optional<Wish> wish = wishGetService.byUser(senior.getUser());
                    return mapToSeniorInfo(senior, salaryStatus, wish.isPresent());
                })
                .toList();
    }

    public CertificationDetailsResponse getCertification(Long seniorId) {
        Senior senior = seniorGetService.bySeniorId(seniorId);
        if (senior.getStatus() == APPROVE)
            throw new SeniorCertificationException();
        return mapToCertificationInfo(senior);
    }


    public void updateNotApprove(Long seniorId) {
        Senior senior = seniorGetService.bySeniorId(seniorId);
        seniorUpdateService.certificationUpdateNotApprove(senior);
    }

    public void updateApprove(Long seniorId) {
        Senior senior = seniorGetService.bySeniorId(seniorId);
        seniorUpdateService.certificationUpdateApprove(senior);
    }

}

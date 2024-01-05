package com.postgraduate.domain.admin.application.usecase;

import com.postgraduate.domain.admin.application.dto.SeniorInfo;
import com.postgraduate.domain.admin.application.dto.req.SeniorStatusRequest;
import com.postgraduate.domain.admin.application.dto.res.CertificationDetailsResponse;
import com.postgraduate.domain.admin.application.dto.res.SeniorManageResponse;
import com.postgraduate.domain.admin.application.mapper.AdminMapper;
import com.postgraduate.domain.admin.exception.SeniorNotWaitingException;
import com.postgraduate.domain.salary.domain.entity.Salary;
import com.postgraduate.domain.admin.presentation.constant.SalaryStatus;
import com.postgraduate.domain.salary.domain.service.SalaryGetService;
import com.postgraduate.domain.senior.domain.entity.Senior;
import com.postgraduate.domain.senior.domain.entity.constant.Status;
import com.postgraduate.domain.senior.domain.service.SeniorGetService;
import com.postgraduate.domain.senior.domain.service.SeniorUpdateService;
import com.postgraduate.domain.wish.domain.entity.Wish;
import com.postgraduate.domain.wish.domain.service.WishGetService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

import static com.postgraduate.domain.salary.util.SalaryUtil.getSalaryDate;
import static com.postgraduate.domain.salary.util.SalaryUtil.getStatus;

@Service
@Transactional
@RequiredArgsConstructor
public class SeniorManageByAdminUseCase {
    private final SeniorGetService seniorGetService;
    private final SeniorUpdateService seniorUpdateService;
    private final SalaryGetService salaryGetService;
    private final WishGetService wishGetService;

    public CertificationDetailsResponse getCertificationDetails(Long seniorId) {
        Senior senior = seniorGetService.bySeniorId(seniorId);
        if (senior.getStatus() != Status.WAITING) {
            throw new SeniorNotWaitingException();
        }
        return AdminMapper.mapToCertificationInfo(senior);
    }

    public void updateSeniorStatus(Long seniorId, SeniorStatusRequest request) {
        Senior senior = seniorGetService.bySeniorId(seniorId);
        seniorUpdateService.updateCertificationStatus(senior, request.certificationStatus());
    }

    public SeniorManageResponse getSeniors(Integer page, String search) {
        Page<Senior> seniors = seniorGetService.all(page, search);
        List<SeniorInfo> seniorInfos = seniors.stream()
                .map(senior -> {
                    Salary salary = salaryGetService.bySenior(senior);
                    SalaryStatus salaryStatus = getStatus(salary);
                    Optional<Wish> wish = wishGetService.byUser(senior.getUser());
                    return AdminMapper.mapToSeniorInfo(senior, salaryStatus, wish.isPresent());
                })
                .toList();
        long totalElements = seniors.getTotalElements();
        int totalPages = seniors.getTotalPages();
        return new SeniorManageResponse(seniorInfos, totalElements, totalPages);
    }
}

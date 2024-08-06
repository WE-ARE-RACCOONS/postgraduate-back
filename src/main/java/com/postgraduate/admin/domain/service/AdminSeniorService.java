package com.postgraduate.admin.domain.service;

import com.postgraduate.admin.application.dto.res.SeniorInfoQuery;
import com.postgraduate.admin.domain.repository.AdminSeniorRepository;
import com.postgraduate.domain.salary.util.SalaryUtil;
import com.postgraduate.domain.senior.domain.entity.Senior;
import com.postgraduate.domain.senior.exception.NoneSeniorException;
import com.postgraduate.domain.senior.exception.SeniorCertificationException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

import static com.postgraduate.domain.senior.domain.entity.constant.Status.*;

@Service
@RequiredArgsConstructor
public class AdminSeniorService {
    private final AdminSeniorRepository adminSeniorRepository;

    public List<SeniorInfoQuery> allSeniors() {
        LocalDate salaryDate = SalaryUtil.getSalaryDate();
        return adminSeniorRepository.allSeniorInfo(salaryDate);
    }

    public Senior bySeniorId(Long seniorId) {
        return getSenior(seniorId);
    }

    public Senior certificationUpdateApprove(Long seniorId) {
        Senior senior = getSenior(seniorId);
        senior.updateStatus(APPROVE);
        return senior;
    }

    public Senior certificationUpdateNotApprove(Long seniorId) {
        Senior senior = getSenior(seniorId);
        senior.updateStatus(NOT_APPROVE);
        return senior;
    }

    private Senior getSenior(Long seniorId) {
        return adminSeniorRepository.findBySeniorId(seniorId)
                .orElseThrow(NoneSeniorException::new);
    }
}

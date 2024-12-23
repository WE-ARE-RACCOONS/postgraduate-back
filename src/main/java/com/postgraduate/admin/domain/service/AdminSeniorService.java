package com.postgraduate.admin.domain.service;

import com.postgraduate.admin.domain.repository.AdminSeniorRepository;
import com.postgraduate.domain.salary.domain.entity.Salary;
import com.postgraduate.domain.salary.util.SalaryUtil;
import com.postgraduate.domain.member.senior.domain.entity.Senior;
import com.postgraduate.domain.member.senior.exception.NoneSeniorException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDate;

import static com.postgraduate.domain.member.senior.domain.entity.constant.Status.*;

@Service
@RequiredArgsConstructor
public class AdminSeniorService {
    private final AdminSeniorRepository adminSeniorRepository;
    private static final int SENIOR_PAGE_SIZE = 10;

    public Page<Salary> allSeniors(Integer page) {
        if (page == null)
            page = 1;
        LocalDate salaryDate = SalaryUtil.getSalaryDate();
        Pageable pageable = PageRequest.of(page-1, SENIOR_PAGE_SIZE);
        return adminSeniorRepository.allSeniorInfo(salaryDate, pageable);
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

package com.postgraduate.admin.domain.service;

import com.postgraduate.admin.domain.repository.AdminSeniorRepository;
import com.postgraduate.domain.salary.domain.entity.Salary;
import com.postgraduate.domain.salary.util.SalaryUtil;
import com.postgraduate.domain.member.senior.domain.entity.Senior;
import com.postgraduate.domain.member.senior.exception.NoneSeniorException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

import static com.postgraduate.domain.member.senior.domain.entity.constant.Status.*;

@Service
@RequiredArgsConstructor
public class AdminSeniorService {
    private final AdminSeniorRepository adminSeniorRepository;

    public List<Salary> allSeniors() {
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

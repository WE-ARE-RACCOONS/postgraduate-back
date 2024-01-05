package com.postgraduate.domain.salary.domain.service;

import com.postgraduate.domain.mentoring.domain.entity.Mentoring;
import com.postgraduate.domain.salary.domain.entity.Salary;
import com.postgraduate.domain.salary.application.dto.SeniorSalary;
import com.postgraduate.domain.salary.domain.repository.SalaryRepository;
import com.postgraduate.domain.salary.exception.SalaryNotFoundException;
import com.postgraduate.domain.salary.util.SalaryUtil;
import com.postgraduate.domain.senior.domain.entity.Senior;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class SalaryGetService {
    private static final int ADMIN_PAGE_SIZE = 15;

    private final SalaryRepository salaryRepository;

    public Salary byMentoring(Mentoring mentoring) {
        return salaryRepository.findByPayments_Mentoring(mentoring).orElseThrow(SalaryNotFoundException::new);
    }

    public Salary bySenior(Senior senior) {
        LocalDate salaryDate = SalaryUtil.getSalaryDate();
        return salaryRepository.findBySeniorAndSalaryDate(senior, salaryDate).orElseThrow(SalaryNotFoundException::new);
    }

    public Salary bySeniorId(Long seniorId) {
        LocalDate salaryDate = SalaryUtil.getSalaryDate();
        return salaryRepository.findBySenior_SeniorIdAndSalaryDate(seniorId, salaryDate).orElseThrow(SalaryNotFoundException::new);
    }

    public Page<SeniorSalary> findDistinctSeniors(String search, Integer page) {
        page = page == null ? 1 : page;
        Pageable pageable = PageRequest.of(page - 1, ADMIN_PAGE_SIZE);
        return salaryRepository.findDistinctBySearchSenior(search, pageable);
    }
}

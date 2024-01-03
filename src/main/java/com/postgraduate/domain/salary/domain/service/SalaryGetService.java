package com.postgraduate.domain.salary.domain.service;

import com.postgraduate.domain.mentoring.domain.entity.Mentoring;
import com.postgraduate.domain.salary.domain.entity.Salary;
import com.postgraduate.domain.salary.application.dto.SeniorSalary;
import com.postgraduate.domain.salary.domain.repository.SalaryRepository;
import com.postgraduate.domain.salary.exception.SalaryNotFoundException;
import com.postgraduate.domain.senior.domain.entity.Senior;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
@RequiredArgsConstructor
public class SalaryGetService {
    private static final int ADMIN_PAGE_SIZE = 15;

    private final SalaryRepository salaryRepository;

    public List<Salary> bySeniorAndStatus(Senior senior, Boolean status) {
        return salaryRepository.findAllBySeniorAndStatus(senior, status);
    }

    public List<Salary> bySeniorAndSalaryDate(Senior senior, LocalDate salaryDate) {
        return salaryRepository.findAllBySeniorAndSalaryDate(senior, salaryDate);
    }

    public List<Salary> bySeniorAndSalaryDateAndStatus(Senior senior, LocalDate salaryDate, Boolean status) {
        return salaryRepository.findAllBySeniorAndSalaryDateAndStatus(senior, salaryDate, status);
    }

    public Salary byMentoring(Mentoring mentoring) {
        return salaryRepository.findByMentoring(mentoring).orElseThrow(SalaryNotFoundException::new);
    }

    public Page<SeniorSalary> findDistinctSeniors(String search, Integer page) {
        page = page == null ? 1 : page;
        Pageable pageable = PageRequest.of(page - 1, ADMIN_PAGE_SIZE);
        return salaryRepository.findDistinctBySearchSenior(search, pageable);
    }
}

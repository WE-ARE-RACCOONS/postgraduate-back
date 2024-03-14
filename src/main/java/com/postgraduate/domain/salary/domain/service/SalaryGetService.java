package com.postgraduate.domain.salary.domain.service;

import com.postgraduate.domain.salary.domain.entity.Salary;
import com.postgraduate.domain.salary.application.dto.SeniorSalary;
import com.postgraduate.domain.salary.domain.repository.SalaryRepository;
import com.postgraduate.domain.salary.exception.SalaryNotFoundException;
import com.postgraduate.domain.salary.util.SalaryUtil;
import com.postgraduate.domain.senior.domain.entity.Senior;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class SalaryGetService {
    private static final int ADMIN_PAGE_SIZE = 15;

    private final SalaryRepository salaryRepository;

    public Salary bySenior(Senior senior) {
        LocalDate salaryDate = SalaryUtil.getSalaryDate();
        log.info("salaryDate : {}", salaryDate);
        return salaryRepository.findBySeniorAndSalaryDate(senior, salaryDate).orElseThrow(SalaryNotFoundException::new);
    }

    public Page<SeniorSalary> findDistinctSeniors(String search, Integer page) {
        page = page == null ? 1 : page;
        Pageable pageable = PageRequest.of(page - 1, ADMIN_PAGE_SIZE);
        return salaryRepository.findDistinctBySearchSenior(search, pageable);
    }

    public List<Salary> findAllLast() {
        LocalDate salaryDate =
                SalaryUtil.getSalaryDate()
                .minusDays(7);
        log.info("salaryDate : {}", salaryDate);
        return salaryRepository.findAllLastSalary(salaryDate);
    }

    public List<Salary> findAll() {
        return salaryRepository.findAll();
    }
}

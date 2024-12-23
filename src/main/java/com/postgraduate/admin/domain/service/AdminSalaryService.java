package com.postgraduate.admin.domain.service;

import com.postgraduate.admin.domain.repository.AdminSalaryRepository;
import com.postgraduate.domain.mentoring.domain.entity.Mentoring;
import com.postgraduate.domain.salary.domain.entity.Salary;
import com.postgraduate.domain.salary.exception.SalaryNotFoundException;
import com.postgraduate.domain.salary.util.SalaryUtil;
import com.postgraduate.domain.member.senior.domain.entity.Senior;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDate;

@Service
@RequiredArgsConstructor
@Slf4j
public class AdminSalaryService {
    private final AdminSalaryRepository adminSalaryRepository;
    private static final int SALARY_PAGE_SIZE = 10;
    public Page<Salary> findAllDoneSalary(Integer page) {
        if (page == null)
            page = 1;
        Pageable pageable = PageRequest.of(page-1, SALARY_PAGE_SIZE);
        return adminSalaryRepository.findAllByDone(pageable);
    }

    public Salary findBySeniorId(Long seniorId) {
        LocalDate salaryDate = SalaryUtil.getSalaryDate();
        return adminSalaryRepository.findBySeniorId(seniorId, salaryDate)
                .orElseThrow(SalaryNotFoundException::new);
    }

    public void updateDone(Long salaryId) {
        Salary salary = adminSalaryRepository.findBySalaryId(salaryId)
                .orElseThrow(SalaryNotFoundException::new);
        salary.updateStatus(true);
    }

    public Page<Salary> findAllByNotDone(Integer page) {
        if (page == null)
            page = 1;
        Pageable pageable = PageRequest.of(page-1, SALARY_PAGE_SIZE);
        LocalDate salaryDate = SalaryUtil.getSalaryDate();
        log.info("salaryDate : {} 확인", salaryDate);
        return adminSalaryRepository.findAllByNotDoneFromLast(salaryDate, pageable);
    }

    public void minusTotalAmount(Mentoring mentoring) {
        Senior senior = mentoring.getSenior();
        Salary salary = findBySeniorId(senior.getSeniorId());
        salary.minusAmount(mentoring.calculateForSenior());
    }
}

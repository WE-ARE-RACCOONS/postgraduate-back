package com.postgraduate.domain.salary.domain.service;

import com.postgraduate.domain.salary.domain.entity.Salary;
import com.postgraduate.domain.salary.domain.repository.SalaryRepository;
import com.postgraduate.domain.senior.domain.entity.Senior;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
@RequiredArgsConstructor
public class SalaryGetService {
    private final SalaryRepository salaryRepository;

    public List<Salary> bySeniorAndStatus(Senior senior, Boolean status) {
        return salaryRepository.findAllBySeniorAndStatus(senior, status);
    }

    public List<Salary> bySeniorAndSalaryDate(Senior senior, LocalDate salaryDate) {
        return salaryRepository.findAllBySeniorAndSalaryDate(senior, salaryDate);
    }
}

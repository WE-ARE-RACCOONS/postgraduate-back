package com.postgraduate.domain.salary.domain.service;

import com.postgraduate.domain.salary.domain.entity.Salary;
import com.postgraduate.domain.salary.domain.repository.SalaryRepository;
import com.postgraduate.domain.senior.domain.entity.Senior;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class SalaryGetService {
    private final SalaryRepository salaryRepository;

    public Optional<Salary> bySeniorAndMonth(Senior senior, String month) {
        return salaryRepository.findBySeniorAndMonth(senior, month);
    }
}
package com.postgraduate.domain.senior.salary.domain.service;

import com.postgraduate.domain.senior.salary.domain.entity.Salary;
import com.postgraduate.domain.senior.salary.domain.repository.SalaryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class SalarySaveService {
    private final SalaryRepository salaryRepository;

    public Salary save(Salary salary) {
        return salaryRepository.save(salary);
    }
}

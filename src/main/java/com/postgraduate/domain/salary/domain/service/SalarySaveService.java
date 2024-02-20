package com.postgraduate.domain.salary.domain.service;

import com.postgraduate.domain.salary.domain.entity.Salary;
import com.postgraduate.domain.salary.domain.repository.SalaryRepository;
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

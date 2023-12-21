package com.postgraduate.domain.salary.domain.repository;

import com.postgraduate.domain.salary.application.dto.SeniorSalary;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface SalaryDslRepository {
    Page<SeniorSalary> findDistinctBySearchSenior(String search, Pageable pageable);
}

package com.postgraduate.domain.salary.domain.repository;

import com.postgraduate.domain.senior.domain.entity.Senior;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.time.LocalDate;

public interface SalaryDslRepository {
    Page<Senior> findDistinctBySearchSenior(LocalDate salaryDate, String search, Pageable pageable);
}

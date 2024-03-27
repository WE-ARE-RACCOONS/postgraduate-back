package com.postgraduate.domain.salary.domain.repository;

import com.postgraduate.domain.salary.application.dto.SeniorSalary;
import com.postgraduate.domain.salary.domain.entity.Salary;
import com.postgraduate.domain.senior.domain.entity.Senior;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.time.LocalDate;
import java.util.List;

public interface SalaryDslRepository {
    Page<SeniorSalary> findDistinctBySearchSenior(String search, Pageable pageable);
    List<Salary> findAllLastSalary(LocalDate salaryDate);
    List<Salary> findAllByNotDoneFromLast(LocalDate salaryDate);
    List<Salary> findAllByDone();
    List<Salary> findAllBySalaryNoneAccount(Senior senior);
}

package com.postgraduate.domain.senior.salary.domain.repository;

import com.postgraduate.domain.senior.salary.domain.entity.Salary;
import com.postgraduate.domain.senior.domain.entity.Senior;
import java.time.LocalDate;
import java.util.List;

public interface SalaryDslRepository {
    List<Salary> findAllLastSalary(LocalDate salaryDate);
    List<Salary> findAllBySalaryNoneAccount(LocalDate salaryDate, Senior senior);
    boolean existIncompleteSalary(Senior senior);
}

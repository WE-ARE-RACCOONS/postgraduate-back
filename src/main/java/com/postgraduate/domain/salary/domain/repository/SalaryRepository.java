package com.postgraduate.domain.salary.domain.repository;

import com.postgraduate.domain.salary.domain.entity.Salary;
import com.postgraduate.domain.senior.domain.entity.Senior;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface SalaryRepository extends JpaRepository<Salary, Long> {
    List<Salary> findAllBySeniorAndSalaryDate(Senior senior, LocalDate salaryDate);
}

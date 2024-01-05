package com.postgraduate.domain.salary.domain.repository;

import com.postgraduate.domain.mentoring.domain.entity.Mentoring;
import com.postgraduate.domain.salary.domain.entity.Salary;
import com.postgraduate.domain.senior.domain.entity.Senior;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface SalaryRepository extends JpaRepository<Salary, Long>, SalaryDslRepository {
    List<Salary> findAllBySeniorAndSalaryDateAndStatus(Senior senior, LocalDate salaryDate, Boolean status);
    Optional<Salary> findBySeniorAndSalaryDateAndStatusIsTrue(Senior senior, LocalDate salaryDate);
    Optional<Salary> findByPayments_Mentoring(Mentoring mentoring);
    Optional<Salary> findBySeniorAndSalaryDate(Senior senior, LocalDate salaryDate);
    Optional<Salary> findBySenior_SeniorIdAndSalaryDate(Long senior, LocalDate salaryDate);
}

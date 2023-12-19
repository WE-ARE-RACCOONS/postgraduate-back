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

public interface SalaryRepository extends JpaRepository<Salary, Long> {
    List<Salary> findAllBySeniorAndSalaryDate(Senior senior, LocalDate salaryDate);
    List<Salary> findAllBySeniorAndSalaryDateAndStatus(Senior senior, LocalDate salaryDate, Boolean status);
    List<Salary> findAllBySeniorAndStatusOrderBySalaryDateDesc(Senior senior, Boolean status);
    Optional<Salary> findByMentoring(Mentoring mentoring);
    @Query("SELECT COUNT(DISTINCT s.senior) FROM Salary s WHERE s.salaryDate = :salaryDate AND s.status = true")
    Long countBySalaryDate(LocalDate salaryDate);
    @Query("SELECT DISTINCT s.senior FROM Salary s WHERE s.salaryDate = :salaryDate AND s.status = true")
    Page<Senior> findDistinctSeniors(LocalDate salaryDate, Pageable pageable);
}

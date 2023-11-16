package com.postgraduate.domain.salary.domain.repository;

import com.postgraduate.domain.salary.domain.entity.Salary;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SalaryInterface extends JpaRepository<Salary, Long> {
}

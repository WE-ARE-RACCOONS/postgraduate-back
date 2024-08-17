package com.postgraduate.admin.domain.service;

import com.postgraduate.admin.domain.repository.AdminSalaryRepository;
import com.postgraduate.domain.mentoring.domain.entity.Mentoring;
import com.postgraduate.domain.salary.domain.entity.Salary;
import com.postgraduate.domain.salary.exception.SalaryNotFoundException;
import com.postgraduate.domain.salary.util.SalaryUtil;
import com.postgraduate.domain.senior.domain.entity.Senior;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
@RequiredArgsConstructor
public class AdminSalaryService {
    private final AdminSalaryRepository adminSalaryRepository;

    public List<Salary> findAllDoneSalary() {
        return adminSalaryRepository.findAllByDone();
    }

    public Salary findBySeniorId(Long seniorId) {
        LocalDate salaryDate = SalaryUtil.getSalaryDate();
        return adminSalaryRepository.findBySeniorId(seniorId, salaryDate)
                .orElseThrow(SalaryNotFoundException::new);
    }

    public void updateDone(Long salaryId) {
        Salary salary = adminSalaryRepository.findBySalaryId(salaryId)
                .orElseThrow(SalaryNotFoundException::new);
        salary.updateStatus(true);
    }

    public List<Salary> findAllByNotDone() {
        LocalDate salaryDate = SalaryUtil.getSalaryDate();
        return adminSalaryRepository.findAllByNotDoneFromLast(salaryDate);
    }

    public void minusTotalAmount(Mentoring mentoring) {
        Senior senior = mentoring.getSenior();
        Salary salary = findBySeniorId(senior.getSeniorId());
        salary.minusAmount(mentoring.calculateForSenior());
    }
}

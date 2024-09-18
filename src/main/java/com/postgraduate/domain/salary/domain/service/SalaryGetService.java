package com.postgraduate.domain.salary.domain.service;

import com.postgraduate.domain.salary.domain.repository.SalaryRepository;
import com.postgraduate.domain.salary.exception.SalaryNotFoundException;
import com.postgraduate.domain.salary.domain.entity.Salary;
import com.postgraduate.domain.salary.util.SalaryUtil;
import com.postgraduate.domain.member.senior.domain.entity.Senior;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class SalaryGetService {

    private final SalaryRepository salaryRepository;

    public Salary bySalaryId(Long salaryId) {
        return salaryRepository.findById(salaryId)
                .orElseThrow(SalaryNotFoundException::new);
    }

    public Salary bySenior(Senior senior) {
        LocalDate salaryDate = SalaryUtil.getSalaryDate();
        return salaryRepository.findBySeniorAndSalaryDateAndSenior_User_IsDeleteIsFalse(senior, salaryDate)
                .orElseThrow(SalaryNotFoundException::new);
    }

    public List<Salary> allBySeniorAndAccountIsNull(Senior senior) {
        LocalDate salaryDate = SalaryUtil.getSalaryDate();
        return salaryRepository.findAllBySalaryNoneAccount(salaryDate, senior);
    }

    public List<Salary> findAllLast() {
        LocalDate salaryDate =
                SalaryUtil.getSalaryDate()
                .minusDays(7);
        log.info("salaryDate : {}", salaryDate);
        return salaryRepository.findAllLastSalary(salaryDate);
    }
}

package com.postgraduate.domain.salary.domain.service;

import com.postgraduate.domain.mentoring.domain.entity.Mentoring;
import com.postgraduate.domain.salary.domain.entity.Salary;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class SalaryUpdateService {
    public void updateAmount(Salary salary, Mentoring mentoring) {
        salary.updateAmount(mentoring.getPay());
    }
}

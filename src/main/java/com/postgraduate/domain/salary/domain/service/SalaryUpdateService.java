package com.postgraduate.domain.salary.domain.service;

import com.postgraduate.domain.salary.domain.entity.Salary;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@RequiredArgsConstructor
public class SalaryUpdateService {

    public void updateStatus(Salary salary, Boolean status) {
        salary.updateStatus(status);
    }
}

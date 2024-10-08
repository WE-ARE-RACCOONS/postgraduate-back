package com.postgraduate.domain.salary.domain.service;

import com.postgraduate.domain.salary.domain.entity.Salary;
import com.postgraduate.domain.salary.domain.entity.SalaryAccount;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class SalaryUpdateService {
    public void updateNot(Salary salary) {
        salary.updateStatus(false);
    }

    public void plusTotalAmount(Salary salary, int amount) {
        salary.plusAmount(amount);
    }

    public void updateAccount(Salary salary, SalaryAccount account) {
        salary.updateAccount(account);
    }
}

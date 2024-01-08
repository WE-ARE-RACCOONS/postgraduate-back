package com.postgraduate.domain.salary.domain.service;

import com.postgraduate.domain.salary.domain.entity.Salary;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class SalaryUpdateService {
    private static int AMOUNT = 20000; //todo : 20000 tnwjd

    public void updateStatus(Salary salary, Boolean status) {
        salary.updateStatus(status);
    }

    public void updateTotalAmount(Salary salary) {
        salary.plusAmount(AMOUNT);
    }

    public void updateAccount(Salary salary, String bank, String accountNumber, String accountHolder) {
        salary.updateAccount(bank, accountNumber, accountHolder);
    }
}

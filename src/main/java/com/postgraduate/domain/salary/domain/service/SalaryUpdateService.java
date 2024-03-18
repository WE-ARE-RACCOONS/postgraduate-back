package com.postgraduate.domain.salary.domain.service;

import com.postgraduate.domain.salary.domain.entity.Salary;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class SalaryUpdateService {
    private static final int AMOUNT = 20000; //todo : 디폴트 20000원 이후 수정

    public void updateDone(Salary salary) {
        salary.updateStatus(true);
    }

    public void updateNot(Salary salary) {
        salary.updateStatus(false);
    }

    public void plusTotalAmount(Salary salary) {
        salary.plusAmount(AMOUNT);
    }

    public void minusTotalAmount(Salary salary) {
        salary.minusAmount(AMOUNT);
    }

    public void updateAccount(Salary salary, String bank, String accountNumber, String accountHolder) {
        salary.updateAccount(bank, accountNumber, accountHolder);
    }
}

package com.postgraduate.domain.admin.application.dto;

import java.time.LocalDateTime;

public record SalaryInfo(
        String nickName,
        String phoneNumber,
        Integer totalAmount,
        String accountHolder,
        String bank,
        String accountNumber,
        LocalDateTime salaryDoneDate
) {
    public SalaryInfo(String nickName, String phoneNumber, int totalAmount, LocalDateTime salaryDoneDate) {
        this(nickName, phoneNumber, totalAmount, null, null, null, salaryDoneDate);
    }
}

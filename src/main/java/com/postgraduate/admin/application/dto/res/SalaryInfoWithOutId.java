package com.postgraduate.admin.application.dto.res;

import java.time.LocalDateTime;

public record SalaryInfoWithOutId(
        String nickName,
        String phoneNumber,
        Integer totalAmount,
        String accountHolder,
        String bank,
        String accountNumber,
        LocalDateTime salaryDoneDate
) {
    public SalaryInfoWithOutId(String nickName, String phoneNumber, int totalAmount, LocalDateTime salaryDoneDate) {
        this(nickName, phoneNumber, totalAmount, null, null, null, salaryDoneDate);
    }
}

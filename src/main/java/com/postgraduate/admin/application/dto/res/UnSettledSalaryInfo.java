package com.postgraduate.admin.application.dto.res;

import java.time.LocalDate;

public record UnSettledSalaryInfo(
        Long salaryId,
        String nickName,
        String phoneNumber,
        Integer totalAmount,
        String accountHolder,
        String bank,
        String accountNumber,
        LocalDate salaryDate
) {
    public UnSettledSalaryInfo(Long salaryId, String nickName, String phoneNumber, int totalAmount, LocalDate salaryDate) {
        this(salaryId, nickName, phoneNumber, totalAmount, null, null, null, salaryDate);
    }
}

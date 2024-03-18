package com.postgraduate.domain.adminssr.application.dto.res;

import java.time.LocalDateTime;

public record SalaryInfo(
        Long salaryId,
        String nickName,
        String phoneNumber,
        Integer totalAmount,
        String accountHolder,
        String bank,
        String accountNumber,
        LocalDateTime salaryDoneDate
) {
    public SalaryInfo(Long salaryId, String nickName, String phoneNumber, int totalAmount, LocalDateTime salaryDoneDate) {
        this(salaryId, nickName, phoneNumber, totalAmount, null, null, null, salaryDoneDate);
    }
}

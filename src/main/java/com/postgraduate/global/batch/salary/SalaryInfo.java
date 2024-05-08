package com.postgraduate.global.batch.salary;

import java.time.LocalDate;

public record SalaryInfo(
        Long seniorId,
        Long accountId,
        String bank,
        String accountNumber,
        String accountHolder
) {
}

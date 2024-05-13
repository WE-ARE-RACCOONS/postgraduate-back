package com.postgraduate.batch.salary;

public record CreateSalary(
        Long seniorId,
        Long accountId,
        String bank,
        String accountNumber,
        String accountHolder
) {
}

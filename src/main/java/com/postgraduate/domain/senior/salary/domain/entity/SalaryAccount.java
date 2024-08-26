package com.postgraduate.domain.senior.salary.domain.entity;

import jakarta.persistence.Embeddable;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Embeddable
public class SalaryAccount {
    private String bank;
    private String accountNumber;
    private String accountHolder;
}

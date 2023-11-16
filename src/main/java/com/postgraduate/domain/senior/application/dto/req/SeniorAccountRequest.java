package com.postgraduate.domain.senior.application.dto.req;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class SeniorAccountRequest {
    private String accountNumber;
    private String bank;
    private String accountHolder;
    private String name;
    private String rrn;
    /**
     *         this.accountNumber = accountNumber;
     *         this.bank = bank;
     *         this.accountHolder = accountHolder;
     *         this.name = name;
     *         this.rrn = rrn;
     */
}

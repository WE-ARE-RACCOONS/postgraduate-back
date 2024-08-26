package com.postgraduate.domain.senior.account.application.mapper;

import com.postgraduate.domain.senior.account.domain.entity.Account;
import com.postgraduate.domain.senior.application.dto.req.SeniorAccountRequest;
import com.postgraduate.domain.senior.application.dto.req.SeniorMyPageUserAccountRequest;
import com.postgraduate.domain.senior.domain.entity.Senior;

public class AccountMapper {
    private AccountMapper() {
        throw new IllegalArgumentException();
    }
    public static Account mapToAccount(Senior senior, SeniorAccountRequest accountRequest, String accountNumber) {
        return Account.builder()
                .senior(senior)
                .accountNumber(accountNumber)
                .accountHolder(accountRequest.accountHolder())
                .bank(accountRequest.bank())
                .build();
    }

    public static Account mapToAccount(Senior senior, SeniorMyPageUserAccountRequest myPageUserAccountRequest, String accountNumber) {
        return Account.builder()
                .senior(senior)
                .accountNumber(accountNumber)
                .accountHolder(myPageUserAccountRequest.accountHolder())
                .bank(myPageUserAccountRequest.bank())
                .build();
    }
}

package com.postgraduate.domain.account.application.mapper;

import com.postgraduate.domain.account.domain.entity.Account;
import com.postgraduate.domain.senior.application.dto.req.SeniorAccountRequest;

public class AccountMapper {
    public static Account mapToAccount(SeniorAccountRequest accountRequest) {
        return Account.builder()
                .accountNumber(accountRequest.getAccountNumber())
                .accountHolder(accountRequest.getAccountHolder())
                .bank(accountRequest.getBank())
                .name(accountRequest.getName())
                .rrn(accountRequest.getRrn())
                .build();
    }
}

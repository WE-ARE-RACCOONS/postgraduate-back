package com.postgraduate.domain.account.application.mapper;

import com.postgraduate.domain.account.domain.entity.Account;
import com.postgraduate.domain.senior.application.dto.req.SeniorAccountRequest;
import com.postgraduate.domain.senior.domain.entity.Senior;

public class AccountMapper {
    public static Account mapToAccount(Senior senior, SeniorAccountRequest accountRequest) {
        return Account.builder()
                .senior(senior)
                .accountNumber(accountRequest.getAccountNumber())
                .accountHolder(accountRequest.getAccountHolder())
                .bank(accountRequest.getBank())
                .name(accountRequest.getName())
                .rrn(accountRequest.getRrn())
                .build();
    }
}

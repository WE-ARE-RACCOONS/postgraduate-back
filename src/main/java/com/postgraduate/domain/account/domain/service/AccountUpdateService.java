package com.postgraduate.domain.account.domain.service;

import com.postgraduate.domain.account.domain.entity.Account;
import com.postgraduate.domain.senior.application.dto.req.SeniorAccountRequest;
import org.springframework.stereotype.Service;

@Service
public class AccountUpdateService {
    public void updateAccount(Account account, SeniorAccountRequest accountRequest) {
        account.updateAccount(accountRequest);
    }
}

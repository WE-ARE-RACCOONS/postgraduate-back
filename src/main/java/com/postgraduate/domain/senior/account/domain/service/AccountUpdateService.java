package com.postgraduate.domain.senior.account.domain.service;

import com.postgraduate.domain.senior.account.domain.entity.Account;
import com.postgraduate.domain.senior.application.dto.req.SeniorMyPageUserAccountRequest;
import org.springframework.stereotype.Service;

@Service
public class AccountUpdateService {
    public void updateAccount(Account account, SeniorMyPageUserAccountRequest myPageUserAccountRequest, String accountNumber) {
        account.updateMyPageUserAccount(myPageUserAccountRequest, accountNumber);
    }
}

package com.postgraduate.domain.account.domain.service;

import com.postgraduate.domain.account.domain.entity.Account;
import com.postgraduate.domain.account.domain.repository.AccountRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AccountSaveService {
    private final AccountRepository accountRepository;

    public void saveAccount(Account account) {
        accountRepository.save(account);
    }
}

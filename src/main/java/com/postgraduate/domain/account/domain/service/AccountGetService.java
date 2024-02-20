package com.postgraduate.domain.account.domain.service;

import com.postgraduate.domain.account.domain.entity.Account;
import com.postgraduate.domain.account.domain.repository.AccountRepository;
import com.postgraduate.domain.senior.domain.entity.Senior;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class AccountGetService {
    private final AccountRepository accountRepository;

    public Optional<Account> bySenior(Senior senior) {
        return accountRepository.findBySenior(senior);
    }
}

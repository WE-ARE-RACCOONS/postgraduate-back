package com.postgraduate.domain.account.domain.service;

import com.postgraduate.domain.senior.account.domain.entity.Account;
import com.postgraduate.domain.senior.account.domain.repository.AccountRepository;
import com.postgraduate.domain.senior.account.domain.service.AccountGetService;
import com.postgraduate.domain.senior.domain.entity.Senior;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mock;

@ExtendWith(MockitoExtension.class)
class AccountGetServiceTest {
    @Mock
    private AccountRepository accountRepository;
    @InjectMocks
    private AccountGetService accountGetService;

    @Test
    @DisplayName("Account존재하는 경우")
    void bySeniorWithNotNull() {
        Senior senior = mock(Senior.class);
        Account account = mock(Account.class);
        given(accountRepository.findBySenior(senior))
                .willReturn(Optional.of(account));

        Assertions.assertThat(accountGetService.bySenior(senior))
                .isEqualTo(Optional.of(account));
    }

    @Test
    @DisplayName("Account존재하는 경우")
    void bySeniorWithNull() {
        Senior senior = mock(Senior.class);
        given(accountRepository.findBySenior(senior))
                .willReturn(Optional.ofNullable(null));

        Assertions.assertThat(accountGetService.bySenior(senior))
                .isEqualTo(Optional.ofNullable(null));
    }
}

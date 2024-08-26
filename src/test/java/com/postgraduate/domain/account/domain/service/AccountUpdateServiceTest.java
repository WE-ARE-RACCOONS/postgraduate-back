package com.postgraduate.domain.account.domain.service;

import com.postgraduate.domain.senior.account.domain.entity.Account;
import com.postgraduate.domain.senior.account.domain.service.AccountUpdateService;
import com.postgraduate.domain.senior.application.dto.req.SeniorMyPageUserAccountRequest;
import com.postgraduate.domain.senior.domain.entity.Senior;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;

@ExtendWith(MockitoExtension.class)
class AccountUpdateServiceTest {
    @InjectMocks
    private AccountUpdateService accountUpdateService;

    @Test
    @DisplayName("계좌 수정 테스트")
    void updateAccount() {
        String accountNumber = "54321";
        Account account = new Account(1L, "123", "신한", "김", mock(Senior.class));
        SeniorMyPageUserAccountRequest request = new SeniorMyPageUserAccountRequest(
                "김", "0101123",
                "abcom", accountNumber,
                "은행", "김시"
        );

        accountUpdateService.updateAccount(account, request, accountNumber);

        assertThat(account.getAccountNumber())
                .isEqualTo(accountNumber);
        assertThat(account.getBank())
                .isEqualTo(request.bank());
        assertThat(account.getAccountHolder())
                .isEqualTo(request.accountHolder());
    }
}
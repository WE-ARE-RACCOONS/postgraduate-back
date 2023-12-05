package com.postgraduate.domain.account.util;

import com.postgraduate.domain.account.domain.entity.Account;
import com.postgraduate.global.config.security.util.EncryptorUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class AccountUtil {
    private final EncryptorUtils encryptorUtils;
    public Account createDummyAccount() {
        return Account.builder()
                .accountNumber(encryptorUtils.encryptData("dummyAccountNumber"))
                .bank("dummyBank")
                .accountHolder("dummyAccountHolder")
                .build();
    }
}

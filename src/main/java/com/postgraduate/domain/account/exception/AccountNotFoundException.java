package com.postgraduate.domain.account.exception;

import static com.postgraduate.domain.account.presentation.constant.AccountResponseCode.ACCOUNT_NOT_FOUND;
import static com.postgraduate.domain.account.presentation.constant.AccountResponseMessage.NOT_FOUND_ACCOUNT;

public class AccountNotFoundException extends AccountException {
    public AccountNotFoundException() {
        super(NOT_FOUND_ACCOUNT.getMessage(), ACCOUNT_NOT_FOUND.getCode());
    }
}

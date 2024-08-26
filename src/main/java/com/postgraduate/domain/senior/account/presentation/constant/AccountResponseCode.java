package com.postgraduate.domain.senior.account.presentation.constant;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
public enum AccountResponseCode {
    ACCOUNT_FIND("ACT200"),
    ACCOUNT_UPDATE("ACT201"),
    ACCOUNT_CREATE("ACT202"),
    ACCOUNT_DELETE("ACT203"),

    ACCOUNT_NOT_FOUND("EX1000");
    private final String code;
}

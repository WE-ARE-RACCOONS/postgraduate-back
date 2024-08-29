package com.postgraduate.domain.member.senior.presentation.constant;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
public enum SeniorResponseCode {
    SENIOR_FIND("SNR200"),
    SENIOR_UPDATE("SNR201"),
    SENIOR_CREATE("SNR202"),
    SENIOR_DELETE("SNR203"),

    SENIOR_NOT_FOUND("EX400"),
    STATUS_NOT_WAITING("EX402"),
    TIME_EMPTY("EX403"),
    INVALID_DAY("EX404"),
    PROFILE_NOT_FOUND("EX405"),
    INVALID_KEYWORD("EX406"),

    ACCOUNT_FIND("ACT200"),
    ACCOUNT_UPDATE("ACT201"),
    ACCOUNT_CREATE("ACT202"),
    ACCOUNT_DELETE("ACT203"),

    ACCOUNT_NOT_FOUND("EX1000");

    private final String code;
}

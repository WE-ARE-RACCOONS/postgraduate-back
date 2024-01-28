package com.postgraduate.domain.senior.presentation.constant;

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
    ACCOUNT_NOT_FOUND("EX401"),
    STATUS_NOT_WAITING("EX402"),
    TIME_EMPTY("EX403"),
    INVALID_DAY("EX404"),
    PROFILE_NOT_FOUND("EX405"),
    INVALID_KEYWORD("EX406"),
    ;
    private final String code;
}

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

    NONE_SENIOR("EX400"),
    NONE_ACCOUNT("EX401"),
    NOT_WAITING_STATUS("EX402");
    private final String code;
}

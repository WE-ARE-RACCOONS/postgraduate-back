package com.postgraduate.domain.senior.presentation.constant;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
public enum SeniorResponseCode {
    SENIOR_FIND("SNR200"),
    SENIOR_UPDATE("SNR201"),
    SENIOR_CREATE("SNR202"),
    SENIOR_DELETE("SNR203");
    private final String code;
}
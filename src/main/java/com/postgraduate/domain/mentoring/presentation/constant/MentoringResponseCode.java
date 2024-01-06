package com.postgraduate.domain.mentoring.presentation.constant;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
public enum MentoringResponseCode {
    MENTORING_FIND("MT200"),
    MENTORING_UPDATE("MT201"),
    MENTORING_CREATE("MT202"),
    MENTORING_DELETE("MT203"),

    MENTORING_NOT_FOUND("EX700"),
    MENTORING_DONE("EX701"),
    MENTORING_NOT_WAITING("EX702"),
    MENTORING_NOT_EXPECTED("EX703"),
    INVALID_DATE("EX704"),
    ;
    private final String code;
}
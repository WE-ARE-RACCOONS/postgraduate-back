package com.postgraduate.domain.mentoring.presentation.constant;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
public enum MentoringResponseCode {
    MENTORING_FIND("MT200"),
    MENTORING_UPDATE("MT201"),
    MENTORING_CREATE("MT202"),
    MENTORING_DELETE("MT203");
    private final String code;
}
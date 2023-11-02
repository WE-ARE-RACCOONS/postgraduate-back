package com.postgraduate.domain.review.presentation.constant;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
public enum ReviewResponseCode {
    REVIEW_FIND("RV200"),
    REVIEW_UPDATE("RV201"),
    REVIEW_CREATE("RV202"),
    REVIEW_DELETE("RV203");
    private final String code;
}

package com.postgraduate.global.auth.quit.domain.entity.constant;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
public enum QuitReason {
    PRIVACY("개인정보 문제"),
    REVENUE("수익창출의 어려움"),
    FEW_JUNIOR("다양하지 않은 후배 풀"),
    FEW_SENIOR("다양하지 않은 선배 풀"),
    DIS_SATISFACTION("멘토링 진행의 불만족"),
    EXPENSE("비용 문제"),
    ETC("기타");

    private final String reason;
}

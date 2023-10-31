package com.postgraduate.domain.mentoring.presentation.constant;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum MentoringResponseMessage {
    GET_MENTORING_LIST_INFO("멘토링 리스트 조회에 성공하였습니다."),
    GET_MENTORING_DETAIL_INFO("멘토링 상세 조회에 성공하였습니다.");

    private final String message;
}

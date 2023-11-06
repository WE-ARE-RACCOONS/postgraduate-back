package com.postgraduate.domain.mentoring.presentation.constant;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum MentoringResponseMessage {
    GET_MENTORING_LIST_INFO("멘토링 리스트 조회에 성공하였습니다."),
    GET_MENTORING_DETAIL_INFO("멘토링 상세 조회에 성공하였습니다."),
    CREATE_MENTORING("멘토링 신청에 성공하였습니다."),
    UPDATE_MENTORING("멘토링 상태 갱신에 성공하였습니다."),

    NOT_FOUND_MENTORING("멘토링이 존재하지 않습니다."),
    DONE_MENTORING("완료된 멘토링입니다.");

    private final String message;
}

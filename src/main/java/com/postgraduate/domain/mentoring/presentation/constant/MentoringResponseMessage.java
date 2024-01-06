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
    NOT_FOUND_DETAIL("볼 수 없는 신청서 입니다."),
    NOT_WAITING_MENTORING("확정 대기 상태의 멘토링이 아닙니다."),
    NOT_EXPECTED_MENTORING("예정 상태의 멘토링이 아닙니다."),
    INVALID_DATE("날짜가 잘못되었습니다."),
    ;

    private final String message;
}

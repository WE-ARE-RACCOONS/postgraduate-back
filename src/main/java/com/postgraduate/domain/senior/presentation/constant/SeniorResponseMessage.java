package com.postgraduate.domain.senior.presentation.constant;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum SeniorResponseMessage {
    CREATE_SENIOR("대학원생 가입에 성공하였습니다."),
    UPDATE_PROFILE("대학원생 프로필 수정에 성공하였습니다"),
    UPDATE_ACCOUNT("대학원생 계좌 수정에 성공하였습니다"),
    CREATE_ACCOUNT("대학원생 계좌 등록에 성공하였습니다"),
    UPDATE_MYPAGE_PROFILE("대학원생 마이페이지 프로필 수정에 성공하였습니다"),
    UPDATE_MYPAGE_ACCOOUNT("대학원생 마이페이지 계정 수정에 성공하였습니다"),
    GET_SENIOR_INFO("대학원생 정보 조회에 성공하였습니다"),
    GET_SENIOR_TIME("대학원생 가능 시간 조회에 성공하였습니다"),
    GET_SENIOR_MYPAGE_PROFILE("대학원생 마이페이지 프로필 조회에 성공하였습니다"),
    GET_SENIOR_MYPAGE_ACCOUNT("대학원생 마이페이지 계정 조회에 성공하였습니다"),
    GET_SENIOR_PROFILE("대학원생 프로필 조회에 성공하였습니다"),
    GET_SENIOR_LIST_INFO("대학원생 리스트 조회에 성공하였습니다."),
    UPDATE_CERTIFICATION("대학원생 인증사진 업로드에 성공하였습니다"),
    UPDATE_STATUS("대학원생 승인 요청 응답에 성공하였습니다"),
    GET_USER_CHECK("후배 변경 가능 여부 확인에 성공하였습니다"),

    NONE_SENIOR("등록된 멘토가 없습니다."),
    NONE_ACCOUNT("계좌가 없습니다."),
    INVALID_KEYWORD("키워드가 잘못되었습니다."),
    NOT_WAITING_STATUS("승인대기 상태의 선배가 아닙니다."),
    EMPTY_TIME("가능 시간이 비었습니다."),
    ;

    private final String message;
}

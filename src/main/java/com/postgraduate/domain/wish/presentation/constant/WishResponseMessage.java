package com.postgraduate.domain.wish.presentation.constant;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum WishResponseMessage {
    GET_WISH_INFO("지원 정보 조회에 성공하였습니다"),
    GET_WISH_LIST_INFO("지원 리스트 조회에 성공하였습니다."),

    NOT_FOUND_WISH("지원을 찾을 수 없습니다."),
    NOT_AGREE_MATCHING("매칭에 동의하지 않았습니다.");

    private final String message;
}

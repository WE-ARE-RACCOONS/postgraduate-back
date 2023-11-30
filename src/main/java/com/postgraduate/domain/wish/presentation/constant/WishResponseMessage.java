package com.postgraduate.domain.wish.presentation.constant;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum WishResponseMessage {
    GET_WISH_INFO("희망 정보 조회에 성공하였습니다"),
    GET_WISH_LIST_INFO("희망 리스트 조회에 성공하였습니다."),

    NOT_FOUND_WISH("희망을 찾을 수 없습니다.");

    private final String message;
}

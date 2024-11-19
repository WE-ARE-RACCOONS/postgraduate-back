package com.postgraduate.domain.wish.presentation.constant;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum WishResponseMessage {
    APPLY_WISH("매칭 신청에 성공하였습니다.");

    private final String message;
}

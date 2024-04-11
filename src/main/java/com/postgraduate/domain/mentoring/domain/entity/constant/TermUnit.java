package com.postgraduate.domain.mentoring.domain.entity.constant;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
public enum TermUnit {
    SHORT(30, 1900);

    private final int term;
    private final int charge;
}

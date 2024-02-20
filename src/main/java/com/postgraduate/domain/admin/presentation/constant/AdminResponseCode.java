package com.postgraduate.domain.admin.presentation.constant;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum AdminResponseCode {
    ADMIN_FIND("ADM200"),
    ADMIN_CREATE("ADM201"),
    ADMIN_UPDATE("ADM202"),
    ADMIN_DELETE("ADM203");

    private final String code;
}

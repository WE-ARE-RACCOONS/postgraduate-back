package com.postgraduate.global.image.presentation.constant;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum ImageResponseCode {
    IMAGE_FIND("IMG200"),
    IMAGE_UPDATE("IMG201"),
    IMAGE_CREATE("IMG202"),
    IMAGE_DELETE("IMG203"),

    IMAGE_EMPTY("EX800"),
    IMAGE_UPLOAD_ERROR("EX801"),
    IMAGE_DELETE_ERROR("EX802");
    private final String code;
}

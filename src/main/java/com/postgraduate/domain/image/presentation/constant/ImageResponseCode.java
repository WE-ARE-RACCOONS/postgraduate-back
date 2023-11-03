package com.postgraduate.domain.image.presentation.constant;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum ImageResponseCode {
    IMAGE_FIND("IMG200"),
    IMAGE_UPDATE("IMG201"),
    IMAGE_CREATE("IMG202"),
    IMAGE_DELETE("IMG203");
    private final String code;
}

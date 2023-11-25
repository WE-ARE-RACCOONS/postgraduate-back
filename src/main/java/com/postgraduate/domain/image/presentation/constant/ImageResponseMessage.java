package com.postgraduate.domain.image.presentation.constant;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum ImageResponseMessage {
    ISSUE_URL("URL발급에 성공하였습니다."),
    UPLOAD_URL("이미지 업로드에 성공하였습니다."),

    EMPTY_IMAGE("이미지 파일이 입력되지 않았습니다."),
    UPLOAD_ERROR("이미지 파일이 업로드되지 않았습니다."),
    DELETE_ERROR("이미지 파일이 삭제되지 않았습니다.");
    private final String message;
}

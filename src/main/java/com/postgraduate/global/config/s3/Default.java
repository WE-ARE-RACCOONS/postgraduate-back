package com.postgraduate.global.config.s3;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
public enum Default {
    USER("https://post-graduate.s3.ap-northeast-2.amazonaws.com/post-graduate-profile/default.png"), SENIOR("https://post-graduate.s3.ap-northeast-2.amazonaws.com/post-graduate-profile/default.png");

    private final String url;
}

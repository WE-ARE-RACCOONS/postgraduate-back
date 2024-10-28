package com.postgraduate.global.slack;

import com.slack.api.model.Field;

public class SlackUtils {
    private SlackUtils() {}

    // Field 생성 메서드
    public static Field generateSlackField(String title, String value) {
        return Field.builder()
                .title(title)
                .value(value)
                .valueShortEnough(false)
                .build();
    }

    public static Field generateSlackField(String title) {
        return Field.builder()
                .title(title)
                .valueShortEnough(false)
                .build();
    }
}

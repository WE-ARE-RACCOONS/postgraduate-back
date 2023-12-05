package com.postgraduate.domain.senior.domain.entity.constant;

import java.util.HashSet;

public enum Postgradu {
    SEOUL("서울대"), YONSEI("연세대");

    private final String postgradu;

    public String getPostgradu() {
        return postgradu;
    }

    Postgradu(String field) {
        this.postgradu = field;
    }

    public static HashSet<String> postgraduNames() {
        HashSet<String> postgraduNames = new HashSet<>();
        for (Postgradu postgradu : Postgradu.values()) {
            postgraduNames.add(postgradu.getPostgradu());
        }
        return postgraduNames;
    }
}

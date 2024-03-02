package com.postgraduate.domain.senior.domain.entity.constant;

import java.util.HashSet;
import java.util.Set;

public enum Postgradu {
    SEOUL("서울대학교"), YONSEI("연세대학교");

    private final String name;

    public String getPostgradu() {
        return name;
    }

    Postgradu(String field) {
        this.name = field;
    }

    public static Set<String> postgraduNames() {
        HashSet<String> postgraduNames = new HashSet<>();
        for (Postgradu postgradu : Postgradu.values()) {
            postgraduNames.add(postgradu.getPostgradu());
        }
        return postgraduNames;
    }
}

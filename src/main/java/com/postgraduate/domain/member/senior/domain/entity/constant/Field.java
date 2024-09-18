package com.postgraduate.domain.member.senior.domain.entity.constant;

import java.util.HashSet;
import java.util.Set;

public enum Field {
    AI("인공지능"), SEMI("반도체"), BIO("바이오"), ENERGY("에너지");

    private final String name;

    public String getField() {
        return name;
    }

    Field(String name) {
        this.name = name;
    }

    public static Set<String> fieldNames() {
        HashSet<String> fieldNames = new HashSet<>();
        for (Field field : Field.values()) {
            fieldNames.add(field.getField());
        }
        return fieldNames;
    }
}

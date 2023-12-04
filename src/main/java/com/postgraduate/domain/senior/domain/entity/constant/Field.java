package com.postgraduate.domain.senior.domain.entity.constant;

import java.util.HashSet;

public enum Field {
    AI("인공지능"), SEMI("반도체"), BIO("바이오"), ENERGY("에너지");

    private final String field;

    public String getField() {
        return field;
    }

    Field(String field) {
        this.field = field;
    }

    public static HashSet<String> fieldNames() {
        HashSet<String> fieldNames = new HashSet<>();
        for (Field field : Field.values()) {
            fieldNames.add(field.getField());
        }
        return fieldNames;
    }
}

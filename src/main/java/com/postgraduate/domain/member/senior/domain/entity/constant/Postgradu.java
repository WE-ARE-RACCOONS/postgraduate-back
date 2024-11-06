package com.postgraduate.domain.member.senior.domain.entity.constant;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.HashSet;
import java.util.Set;

@RequiredArgsConstructor
@Getter
public enum Postgradu {
    SEOUL("서울대학교"), YONSEI("연세대학교"), KOREA("고려대학교"), KAIST("카이스트");

    private final String name;

    public static Set<String> postgraduNames() {
        HashSet<String> postgraduNames = new HashSet<>();
        for (Postgradu postgradu : Postgradu.values()) {
            postgraduNames.add(postgradu.getName());
        }
        return postgraduNames;
    }
}

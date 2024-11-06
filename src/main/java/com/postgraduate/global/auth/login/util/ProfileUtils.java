package com.postgraduate.global.auth.login.util;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class ProfileUtils {
    @Value("${profile.senior.default1}")
    private String senior1;
    @Value("${profile.senior.default2}")
    private String senior2;
    @Value("${profile.senior.default3}")
    private String senior3;
    @Value("${profile.senior.default4}")
    private String senior4;
    @Value("${profile.senior.default5}")
    private String senior5;

    @Value("${profile.junior.default}")
    private String junior;

    public String juniorProfile() {
        return junior;
    }

    public String seniorProfile(int num) {
        if (num == 0)
            return senior1;
        if (num == 1)
            return senior2;
        if (num == 2)
            return senior3;
        if (num == 3)
            return senior4;
        return senior5;
    }

    public List<String> allProfiles() {
        return List.of(senior1, senior2, senior3, senior4, senior5, junior);
    }
}

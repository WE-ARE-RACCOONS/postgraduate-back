package com.postgraduate.domain.auth.util;

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

    @Value("${profile.junior.default1}")
    private String junior1;
    @Value("${profile.junior.default2}")
    private String junior2;
    @Value("${profile.junior.default3}")
    private String junior3;
    @Value("${profile.junior.default4}")
    private String junior4;
    @Value("${profile.junior.default5}")
    private String junior5;

    public String juniorProfile(int num) {
        if (num == 0)
            return junior1;
        if (num == 1)
            return junior2;
        if (num == 2)
            return junior3;
        if (num == 3)
            return junior4;
        return junior5;
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
        return List.of(senior1, senior2, senior3, senior4, senior5, junior1, junior2, junior3, junior4, junior5);
    }
}

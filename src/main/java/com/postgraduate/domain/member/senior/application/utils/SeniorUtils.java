package com.postgraduate.domain.member.senior.application.utils;

import com.postgraduate.domain.member.senior.exception.KeywordException;
import org.springframework.stereotype.Component;

@Component
public class SeniorUtils {
    public void checkKeyword(String keyword) {
        String[] keywordCount = keyword.split(",");
        if (keywordCount.length > 6)
            throw new KeywordException();
    }
}

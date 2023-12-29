package com.postgraduate.domain.senior.application.utils;

import com.postgraduate.domain.senior.exception.KeywordException;
import org.springframework.stereotype.Component;

@Component
public class SeniorUtils {
    public void checkKeyword(String keyword) {
        String[] keywordCount = keyword.split(",");
        if (keywordCount.length > 6)
            throw new KeywordException();
    }
}

package com.postgraduate.domain.senior.application.utils;

import com.postgraduate.domain.senior.exception.KeywordException;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class SeniorUtilsTest {
    @InjectMocks
    SeniorUtils seniorUtils;

    @ParameterizedTest
    @ValueSource(strings = {"a,b,c,d,e,f,g","a,b,c,d,e,f,g,h","a,b,c,d,e,f,g,h,i"})
    @DisplayName("키워드 예외 테스트")
    void invalidKeyword(String keywords) {
        assertThatThrownBy(() -> seniorUtils.checkKeyword(keywords))
                .isInstanceOf(KeywordException.class);
    }

    @ParameterizedTest
    @ValueSource(strings = {"a,b,c,d,e,f","1,2,3,4,5,6","abcd,efg,abc,qwe,123,agd"})
    @DisplayName("키워드 성공 테스트")
    void KeywordTest(String keywords) {
        assertDoesNotThrow(() -> seniorUtils.checkKeyword(keywords));
    }
}
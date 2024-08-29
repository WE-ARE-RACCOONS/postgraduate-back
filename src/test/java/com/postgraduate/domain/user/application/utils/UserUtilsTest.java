package com.postgraduate.domain.user.application.utils;

import com.postgraduate.domain.member.user.application.utils.UserUtils;
import com.postgraduate.domain.member.user.exception.PhoneNumberException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class UserUtilsTest {
    @InjectMocks
    UserUtils userUtils;

    @ParameterizedTest
    @ValueSource(strings = {"0101234123", "0101234", "010123412341", "010", "0101234123412", "abcabcdabcd"})
    @DisplayName("핸드폰 번호 예외 테스트")
    void invalidPhoneNumber(String phoneNumber) {
        assertThatThrownBy(() -> userUtils.checkPhoneNumber(phoneNumber))
                .isInstanceOf(PhoneNumberException.class);
    }

    @ParameterizedTest
    @ValueSource(strings = {"01012341234", "01012344321"})
    @DisplayName("핸드폰 번호 성공 테스트")
    void phoneNumberTest(String phoneNumber) {
        assertDoesNotThrow(() -> userUtils.checkPhoneNumber(phoneNumber));
    }
}
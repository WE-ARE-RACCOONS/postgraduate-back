package com.postgraduate.domain.user.domain.service;

import com.postgraduate.domain.user.domain.repository.UserRepository;
import com.postgraduate.domain.user.exception.UserNotFoundException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.BDDMockito.given;

@ExtendWith(MockitoExtension.class)
class UserGetServiceTest {
    @Mock
    private UserRepository userRepository;
    @InjectMocks
    private UserGetService userGetService;

    @Test
    @DisplayName("User 조회 예외 테스트")
    void byUserIdFail() {
        long userId = 1L;
        given(userRepository.findById(userId))
                .willReturn(Optional.ofNullable(null));

        assertThatThrownBy(() -> userGetService.getUser(userId))
                .isInstanceOf(UserNotFoundException.class);
    }
}
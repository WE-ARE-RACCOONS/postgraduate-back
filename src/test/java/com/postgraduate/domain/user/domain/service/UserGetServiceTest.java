package com.postgraduate.domain.user.domain.service;

import com.postgraduate.domain.user.user.domain.entity.User;
import com.postgraduate.domain.user.user.domain.repository.UserRepository;
import com.postgraduate.domain.user.user.domain.service.UserGetService;
import com.postgraduate.domain.user.user.exception.UserNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.Optional;

import static com.postgraduate.domain.user.user.domain.entity.constant.Role.USER;
import static java.lang.Boolean.TRUE;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.BDDMockito.given;

@ExtendWith(MockitoExtension.class)
class UserGetServiceTest {
    @Mock
    private UserRepository userRepository;
    @InjectMocks
    private UserGetService userGetService;

    private User user;
    @BeforeEach
    void setting() {
        user = new User(-1l, -1l, "a",
                "a", "123", "a",
                1, USER, TRUE, LocalDateTime.now(), LocalDateTime.now(), TRUE);
    }

    @Test
    @DisplayName("userId 조회 예외 테스트")
    void byUserIdFail() {
        given(userRepository.findById(user.getUserId()))
                .willReturn(Optional.ofNullable(null));

        assertThatThrownBy(() -> userGetService.byUserId(user.getUserId()))
                .isInstanceOf(UserNotFoundException.class);
    }

    @Test
    @DisplayName("userId 조회 테스트")
    void byUserId() {
        given(userRepository.findById(user.getUserId()))
                .willReturn(Optional.of(user));

        User result = userGetService.byUserId(user.getUserId());

        assertThat(result)
                .isEqualTo(user);
    }

    @Test
    @DisplayName("socialId 조회 예외 테스트")
    void bySocialIdFail() {
        given(userRepository.findBySocialId(user.getSocialId()))
                .willReturn(Optional.ofNullable(null));

        assertThatThrownBy(() -> userGetService.bySocialId(user.getSocialId()))
                .isInstanceOf(UserNotFoundException.class);
    }

    @Test
    @DisplayName("socialId 조회 테스트")
    void bySocialId() {
        given(userRepository.findBySocialId(user.getSocialId()))
                .willReturn(Optional.of(user));

        User result = userGetService.bySocialId(user.getSocialId());

        assertThat(result)
                .isEqualTo(user);
    }

    @Test
    @DisplayName("nickName 조회 테스트")
    void byNickNameFail() {
        given(userRepository.findByNickName(user.getNickName()))
                .willReturn(Optional.ofNullable(null));

        Optional<User> result =
                userGetService.byNickName(user.getNickName());

        assertThat(result)
                .isEqualTo(Optional.ofNullable(null));
    }

    @Test
    @DisplayName("nickName 조회 테스트")
    void byNickName() {
        given(userRepository.findByNickName(user.getNickName()))
                .willReturn(Optional.of(user));

        Optional<User> result =
                userGetService.byNickName(user.getNickName());

        assertThat(result.get())
                .isNotNull();
    }
}
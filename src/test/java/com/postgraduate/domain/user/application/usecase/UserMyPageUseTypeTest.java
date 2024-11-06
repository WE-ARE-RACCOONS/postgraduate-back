package com.postgraduate.domain.user.application.usecase;

import com.postgraduate.domain.member.user.application.dto.res.UserInfoResponse;
import com.postgraduate.domain.member.user.application.dto.res.UserMyPageResponse;
import com.postgraduate.domain.member.user.application.dto.res.UserPossibleResponse;
import com.postgraduate.domain.member.user.application.usecase.UserMyPageUseCase;
import com.postgraduate.domain.member.user.domain.entity.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;

import static com.postgraduate.domain.member.user.domain.entity.constant.Role.SENIOR;
import static com.postgraduate.domain.member.user.domain.entity.constant.Role.USER;
import static java.lang.Boolean.TRUE;
import static java.time.LocalDate.now;
import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith(MockitoExtension.class)
class UserMyPageUseTypeTest {
    @InjectMocks
    UserMyPageUseCase userMyPageUseCase;

    private User user;
    @BeforeEach
    void setting() {
        user = new User(1L, 1234L, "a",
                "a", "123", "a",
                1, USER, TRUE, LocalDateTime.now(), LocalDateTime.now(), TRUE, TRUE, null);
    }

    @Test
    @DisplayName("마이페이지에서의 유저 정보 확인 테스트")
    void getMyPageUserInfo() {
        UserMyPageResponse userInfo = userMyPageUseCase.getUserInfo(user);

        assertThat(userInfo.nickName())
                .isEqualTo(user.getNickName());
        assertThat(userInfo.profile())
                .isEqualTo(user.getProfile());
    }

    @Test
    @DisplayName("유저 정보 확인 테스트")
    void getUserInfo() {
        UserInfoResponse userInfo = userMyPageUseCase.getUserOriginInfo(user);

        assertThat(userInfo.nickName())
                .isEqualTo(user.getNickName());
        assertThat(userInfo.profile())
                .isEqualTo(user.getProfile());
        assertThat(userInfo.phoneNumber())
                .isEqualTo(user.getPhoneNumber());
    }

    @Test
    @DisplayName("선배 여부 확인 - USER")
    void checkSeniorWithUser() {
        UserPossibleResponse checkSenior = userMyPageUseCase.checkSenior(user);

        assertThat(checkSenior.possible())
                .isFalse();
        assertThat(checkSenior.socialId())
                .isEqualTo(user.getSocialId());
    }

    @Test
    @DisplayName("선배 여부 확인 - SENIOR")
    void checkSeniorWithSenior() {
        user = new User(1L, 1234L, "a",
                "a", "123", "a",
                1, SENIOR, TRUE, LocalDateTime.now(), LocalDateTime.now(), TRUE, TRUE, null);

        UserPossibleResponse checkSenior = userMyPageUseCase.checkSenior(user);

        assertThat(checkSenior.possible())
                .isTrue();
        assertThat(checkSenior.socialId())
                .isEqualTo(user.getSocialId());
    }
}
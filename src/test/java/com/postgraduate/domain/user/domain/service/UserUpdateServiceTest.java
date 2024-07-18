package com.postgraduate.domain.user.domain.service;

import com.postgraduate.domain.auth.util.ProfileUtils;
import com.postgraduate.domain.senior.application.dto.req.SeniorMyPageUserAccountRequest;
import com.postgraduate.domain.user.user.application.dto.req.UserInfoRequest;
import com.postgraduate.domain.user.user.domain.entity.User;
import com.postgraduate.domain.user.user.domain.service.UserUpdateService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static java.lang.Boolean.FALSE;
import static java.lang.Boolean.TRUE;
import static java.time.LocalDateTime.now;
import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith(MockitoExtension.class)
class UserUpdateServiceTest {
    @Mock
    ProfileUtils profileUtils;

    @InjectMocks
    private UserUpdateService userUpdateService;

    private User user;

    @BeforeEach
    void setting() {
        user = new User(1L, 2L, "a",
                "b", "c", "d",
                0, USER, FALSE,
                now(), now(), TRUE);
    }

    @Test
    @DisplayName("삭제 테스트")
    void delete() {
        userUpdateService.updateDelete(user);

        assertThat(user.isDelete())
                .isTrue();
    }

    @Test
    @DisplayName("SENIOR 변경 테스트")
    void updateRoleWithSenior() {
        userUpdateService.userToSeniorRole(user, 1);

        assertThat(user.getRole())
                .isEqualTo(SENIOR);
    }

    @Test
    @DisplayName("정보 수정 테스트")
    void updateInfo() {
        UserInfoRequest request = new UserInfoRequest("profile", "nick", "phone");
        userUpdateService.updateInfo(user, request);

        assertThat(user.getProfile())
                .isEqualTo(request.profile());
        assertThat(user.getProfile())
                .isEqualTo(request.profile());
        assertThat(user.getProfile())
                .isEqualTo(request.profile());
    }

    @Test
    @DisplayName("Senior 계정 설정 테스트")
    void updateSeniorUserAccount() {
        SeniorMyPageUserAccountRequest request = new SeniorMyPageUserAccountRequest(
                "nick", "phone", "profile",
                "num", "bank", "holder");
        userUpdateService.updateSeniorUserAccount(user, request);

        assertThat(user.getProfile())
                .isEqualTo(request.profile());
        assertThat(user.getNickName())
                .isEqualTo(request.nickName());
        assertThat(user.getPhoneNumber())
                .isEqualTo(request.phoneNumber());
    }
}
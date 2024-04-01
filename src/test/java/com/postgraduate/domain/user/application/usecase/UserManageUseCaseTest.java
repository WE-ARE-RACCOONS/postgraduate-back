package com.postgraduate.domain.user.application.usecase;

import com.postgraduate.domain.user.application.dto.req.UserInfoRequest;
import com.postgraduate.domain.user.application.utils.UserUtils;
import com.postgraduate.domain.user.domain.entity.User;
import com.postgraduate.domain.user.domain.service.UserGetService;
import com.postgraduate.domain.user.domain.service.UserUpdateService;
import com.postgraduate.domain.user.exception.PhoneNumberException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserManageUseCaseTest {
    @Mock
    UserUpdateService userUpdateService;
    @Mock
    UserGetService userGetService;
    @Mock
    UserUtils userUtils;
    @InjectMocks
    UserManageUseCase userManageUseCase;

    @Test
    @DisplayName("phoenumber 예외 테스트")
    void updateInfoWithInvalidPhone() {
        User user = mock(User.class);
        UserInfoRequest request = new UserInfoRequest("a", "a", "b");

        doThrow(PhoneNumberException.class)
                .when(userUtils)
                .checkPhoneNumber(request.phoneNumber());

        assertThatThrownBy(() -> userManageUseCase.updateInfo(user, request))
                .isInstanceOf(PhoneNumberException.class);
    }

    @Test
    @DisplayName("유저 업데이트 확인 테스트")
    void updateInfo() {
        User user = mock(User.class);
        UserInfoRequest request = mock(UserInfoRequest.class);

        given(userGetService.byUserId(any(Long.class)))
                .willReturn(user);
        userManageUseCase.updateInfo(user, request);

        verify(userUpdateService)
                .updateInfo(user, request);
    }
}
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
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.mock;

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
    void invalidPhoneNumber() {
        User user = mock(User.class);
        UserInfoRequest request = new UserInfoRequest("a", "b", "c");

        doThrow(PhoneNumberException.class)
                .when(userUtils)
                .checkPhoneNumber(request.phoneNumber());

        assertThatThrownBy(() -> userManageUseCase.updateInfo(user, request))
                .isInstanceOf(PhoneNumberException.class);
    }
}
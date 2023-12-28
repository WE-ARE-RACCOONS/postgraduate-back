package com.postgraduate.domain.senior.application.usecase;

import com.postgraduate.domain.account.domain.entity.Account;
import com.postgraduate.domain.account.domain.service.AccountGetService;
import com.postgraduate.domain.account.domain.service.AccountSaveService;
import com.postgraduate.domain.account.domain.service.AccountUpdateService;
import com.postgraduate.domain.available.application.dto.req.AvailableCreateRequest;
import com.postgraduate.domain.available.domain.entity.Available;
import com.postgraduate.domain.available.domain.service.AvailableDeleteService;
import com.postgraduate.domain.available.domain.service.AvailableSaveService;
import com.postgraduate.domain.senior.application.dto.req.SeniorMyPageProfileRequest;
import com.postgraduate.domain.senior.application.dto.req.SeniorMyPageUserAccountRequest;
import com.postgraduate.domain.senior.application.dto.req.SeniorProfileRequest;
import com.postgraduate.domain.senior.application.utils.SeniorUtils;
import com.postgraduate.domain.senior.domain.entity.Info;
import com.postgraduate.domain.senior.domain.entity.Profile;
import com.postgraduate.domain.senior.domain.entity.Senior;
import com.postgraduate.domain.senior.domain.service.SeniorGetService;
import com.postgraduate.domain.senior.domain.service.SeniorUpdateService;
import com.postgraduate.domain.senior.exception.KeywordException;
import com.postgraduate.domain.user.application.utils.UserUtils;
import com.postgraduate.domain.user.domain.entity.User;
import com.postgraduate.domain.user.domain.service.UserGetService;
import com.postgraduate.domain.user.domain.service.UserUpdateService;
import com.postgraduate.domain.user.exception.PhoneNumberException;
import com.postgraduate.global.config.security.util.EncryptorUtils;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static com.postgraduate.domain.senior.domain.entity.constant.Status.APPROVE;
import static com.postgraduate.domain.user.domain.entity.constant.Role.USER;
import static java.lang.Boolean.TRUE;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.BDDMockito.*;

@ExtendWith(MockitoExtension.class)
class SeniorManageUseCaseTest {
    @Mock
    private UserUpdateService userUpdateService;
    @Mock
    private UserGetService userGetService;
    @Mock
    private SeniorUpdateService seniorUpdateService;
    @Mock
    private SeniorGetService seniorGetService;
    @Mock
    private AvailableSaveService availableSaveService;
    @Mock
    private AvailableDeleteService availableDeleteService;
    @Mock
    private AccountGetService accountGetService;
    @Mock
    private AccountSaveService accountSaveService;
    @Mock
    private AccountUpdateService accountUpdateService;
    @Mock
    private EncryptorUtils encryptorUtils;
    @Mock
    private UserUtils userUtils;
    @Mock
    private SeniorUtils seniorUtils;
    @InjectMocks
    private SeniorManageUseCase seniorManageUseCase;

    private User user;
    private Senior senior;
    private Info info;
    private Profile profile;

    @BeforeEach
    void setting() {
        info = new Info("a", "a", "a", "a", "a", "a", TRUE, TRUE, "a");
        profile = new Profile("a", "a", "a", "a", 40);
        user = new User(1L, 1234L, "a",
                "a", "123", "a",
                1, USER, TRUE, LocalDateTime.now(), LocalDateTime.now(), TRUE);
        senior = new Senior(1L, user, "a",
                APPROVE, 1, info, profile,
                LocalDateTime.now(), LocalDateTime.now());
    }

    @Test
    @DisplayName("선배 프로필 정보 업데이트")
    void signUpProfile() {
        AvailableCreateRequest available1 = new AvailableCreateRequest("월", "12:00", "18:00");
        AvailableCreateRequest available2 = new AvailableCreateRequest("화", "12:00", "18:00");
        AvailableCreateRequest available3 = new AvailableCreateRequest("수", "12:00", "18:00");
        List<AvailableCreateRequest> availableCreateRequests = List.of(available1, available2, available3);
        SeniorProfileRequest request = new SeniorProfileRequest(
                profile.getInfo(), profile.getTarget(),
                profile.getChatLink(), profile.getOneLiner(),
                availableCreateRequests);

        given(seniorGetService.byUser(user))
                .willReturn(senior);

        seniorManageUseCase.signUpProfile(user, request);

        verify(seniorUpdateService)
                .signUpSeniorProfile(eq(senior), any(Profile.class));
        verify(availableSaveService, times(availableCreateRequests.size()))
                .save(any(Available.class));
    }

    @Test
    @DisplayName("Account없는 경우 계정 수정 테스트")
    void updateSeniorMyPageUserAccountWithNonAccount() {
        SeniorMyPageUserAccountRequest request =
                new SeniorMyPageUserAccountRequest("a", "b", "a" , "b", "a", "b");

        given(userGetService.getUser(user.getUserId()))
                .willReturn(user);
        given(seniorGetService.byUser(user))
                .willReturn(senior);
        given(accountGetService.bySenior(senior))
                .willReturn(Optional.ofNullable(null));
        seniorManageUseCase.updateSeniorMyPageUserAccount(user, request);

        verify(userUpdateService)
                .updateSeniorUserAccount(user, request);
        verify(accountSaveService)
                .saveAccount(any(Account.class));
    }

    @Test
    @DisplayName("Account있는 경우 계정 수정 테스트")
    void updateSeniorMyPageUserAccountWithAccount() {
        SeniorMyPageUserAccountRequest request =
                new SeniorMyPageUserAccountRequest("a", "b", "a" , "b", "a", "b");

        given(userGetService.getUser(user.getUserId()))
                .willReturn(user);
        given(seniorGetService.byUser(user))
                .willReturn(senior);
        given(accountGetService.bySenior(senior))
                .willReturn(Optional.of(mock(Account.class)));
        given(encryptorUtils.encryptData(request.accountNumber()))
                .willReturn("encrypt");

        seniorManageUseCase.updateSeniorMyPageUserAccount(user, request);

        verify(userUpdateService)
                .updateSeniorUserAccount(user, request);
        verify(accountUpdateService)
                .updateAccount(any(Account.class), eq(request), eq("encrypt"));
    }

    @Test
    @DisplayName("키워드 예외 테스트")
    void invalidKeyword() {
        SeniorMyPageProfileRequest request = mock(SeniorMyPageProfileRequest.class);

        doThrow(KeywordException.class)
                .when(seniorUtils)
                .checkKeyword(any());

        assertThatThrownBy(() -> seniorManageUseCase.updateSeniorMyPageProfile(user, request))
                .isInstanceOf(KeywordException.class);
    }

    @Test
    @DisplayName("번호 예외 테스트")
    void userAccountInvalidPhoneNumber() {
        SeniorMyPageUserAccountRequest request =
                new SeniorMyPageUserAccountRequest("a", "1234", "a" , "b", "a", "b");

        doThrow(PhoneNumberException.class)
                .when(userUtils)
                .checkPhoneNumber(request.phoneNumber());

        assertThatThrownBy(() -> seniorManageUseCase.updateSeniorMyPageUserAccount(user, request))
                .isInstanceOf(PhoneNumberException.class);
    }
}
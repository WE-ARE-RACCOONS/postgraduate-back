package com.postgraduate.domain.senior.application.usecase;

import com.postgraduate.domain.senior.account.domain.entity.Account;
import com.postgraduate.domain.senior.account.domain.service.AccountGetService;
import com.postgraduate.domain.senior.account.domain.service.AccountSaveService;
import com.postgraduate.domain.senior.account.domain.service.AccountUpdateService;
import com.postgraduate.domain.senior.available.application.dto.req.AvailableCreateRequest;
import com.postgraduate.domain.senior.available.domain.entity.Available;
import com.postgraduate.domain.senior.available.domain.service.AvailableDeleteService;
import com.postgraduate.domain.senior.available.domain.service.AvailableSaveService;
import com.postgraduate.domain.senior.salary.application.mapper.SalaryMapper;
import com.postgraduate.domain.senior.salary.domain.entity.Salary;
import com.postgraduate.domain.senior.salary.domain.service.SalaryGetService;
import com.postgraduate.domain.senior.salary.domain.service.SalaryUpdateService;
import com.postgraduate.domain.senior.application.dto.req.*;
import com.postgraduate.domain.senior.application.dto.res.SeniorProfileUpdateResponse;
import com.postgraduate.domain.senior.application.utils.SeniorUtils;
import com.postgraduate.domain.senior.domain.entity.Info;
import com.postgraduate.domain.senior.domain.entity.Profile;
import com.postgraduate.domain.senior.domain.entity.Senior;
import com.postgraduate.domain.senior.domain.service.SeniorGetService;
import com.postgraduate.domain.senior.domain.service.SeniorUpdateService;
import com.postgraduate.domain.senior.exception.KeywordException;
import com.postgraduate.domain.user.user.application.utils.UserUtils;
import com.postgraduate.domain.user.user.domain.entity.User;
import com.postgraduate.domain.user.user.domain.service.UserUpdateService;
import com.postgraduate.domain.user.user.exception.PhoneNumberException;
import com.postgraduate.global.config.security.util.EncryptorUtils;
import com.postgraduate.global.slack.SlackCertificationMessage;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static com.postgraduate.domain.senior.domain.entity.constant.Status.APPROVE;
import static com.postgraduate.domain.user.user.domain.entity.constant.Role.SENIOR;
import static java.lang.Boolean.TRUE;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.BDDMockito.*;

@ExtendWith(MockitoExtension.class)
class SeniorManageUseTypeTest {
    @Mock
    private UserUpdateService userUpdateService;
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
    private SalaryGetService salaryGetService;
    @Mock
    private SalaryUpdateService salaryUpdateService;
    @Mock
    private SalaryMapper salaryMapper;
    @Mock
    private EncryptorUtils encryptorUtils;
    @Mock
    private UserUtils userUtils;
    @Mock
    private SeniorUtils seniorUtils;
    @Mock
    private SlackCertificationMessage slackCertificationMessage;
    @InjectMocks
    private SeniorManageUseCase seniorManageUseCase;

    private User user;
    private Senior senior;
    private Info info;
    private Profile profile;

    @BeforeEach
    void setting() {
        info = new Info("a", "a", "a", "a", "a", "a", TRUE, TRUE, "a", "chatLink", 30);
        profile = new Profile("a", "a", "a");
        user = new User(1L, 1234L, "a",
                "a", "123", "a",
                1, SENIOR, TRUE, LocalDateTime.now(), LocalDateTime.now(), TRUE, TRUE);
        senior = new Senior(1L, user, "a",
                APPROVE, 1, 1, info, profile,
                LocalDateTime.now(), LocalDateTime.now());
    }

    @Test
    @DisplayName("선배 인증 업데이트")
    void updateCertification() {
        SeniorCertificationRequest request = new SeniorCertificationRequest("anyThing");
        given(seniorGetService.byUser(user))
                .willReturn(senior);
        seniorManageUseCase.updateCertification(user, request);

        verify(seniorUpdateService, times(1))
                .updateCertification(senior, request.certification());
    }

    @Test
    @DisplayName("선배 프로필 정보 업데이트")
    void signUpProfile() {
        AvailableCreateRequest available1 = new AvailableCreateRequest("월", "12:00", "18:00");
        AvailableCreateRequest available2 = new AvailableCreateRequest("화", "12:00", "18:00");
        AvailableCreateRequest available3 = new AvailableCreateRequest("수", "12:00", "18:00");
        List<AvailableCreateRequest> availableCreateRequests = List.of(available1, available2, available3);
        SeniorProfileRequest request = new SeniorProfileRequest(
                profile.getInfo(), profile.getTarget(), profile.getOneLiner(),
                availableCreateRequests);

        given(seniorGetService.byUser(user))
                .willReturn(senior);

        seniorManageUseCase.signUpProfile(user, request);

        verify(seniorUpdateService, times(1))
                .signUpSeniorProfile(eq(senior), any(Profile.class));
        verify(availableSaveService, times(availableCreateRequests.size()))
                .save(any(Available.class));
    }

    @Test
    @DisplayName("Account없는 경우 계정 수정 테스트")
    void updateSeniorMyPageUserAccountWithNonAccount() {
        SeniorMyPageUserAccountRequest request =
                new SeniorMyPageUserAccountRequest("a", "b", "a", "b", "a", "b");

        given(seniorGetService.byUserWithAll(user))
                .willReturn(senior);
        given(accountGetService.bySenior(senior))
                .willReturn(Optional.ofNullable(null));
        given(encryptorUtils.encryptData(request.accountNumber()))
                .willReturn("encrypt");
        given(salaryGetService.allBySeniorAndAccountIsNull(senior))
                .willReturn(List.of(mock(Salary.class)));

        seniorManageUseCase.updateSeniorMyPageUserAccount(user, request);

        verify(userUpdateService)
                .updateSeniorUserAccount(user, request);
        verify(accountSaveService)
                .save(any(Account.class));
    }

    @Test
    @DisplayName("계좌 생성 테스트")
    void saveAccount() {
        SeniorAccountRequest request = new SeniorAccountRequest("a", "a", "a");
        given(seniorGetService.byUser(user))
                .willReturn(senior);

        seniorManageUseCase.saveAccount(user, request);

        verify(accountSaveService, times(1))
                .save(any(Account.class));
    }

    @Test
    @DisplayName("마이페이지 프로필 업데이트 키워드 예외 테스트")
    void invalidKeyword() {
        SeniorMyPageProfileRequest request = mock(SeniorMyPageProfileRequest.class);

        doThrow(KeywordException.class)
                .when(seniorUtils)
                .checkKeyword(any());

        assertThatThrownBy(() -> seniorManageUseCase.updateSeniorMyPageProfile(user, request))
                .isInstanceOf(KeywordException.class);
    }

    @Test
    @DisplayName("마이페이지 프로필 업데이트 테스트")
    void updateSeniorMyPage() {
        SeniorMyPageProfileRequest request =
                mock(SeniorMyPageProfileRequest.class);
        given(request.field())
                .willReturn("a,b,c");
        given(request.keyword())
                .willReturn("a,b,c");
        given(seniorGetService.byUser(user))
                .willReturn(senior);

        SeniorProfileUpdateResponse response = seniorManageUseCase.updateSeniorMyPageProfile(user, request);

        verify(seniorUpdateService, times(1))
                .updateMyPageProfile(any(Senior.class), any(Info.class), any(Profile.class));
        verify(availableDeleteService, times(1))
                .delete(senior);
        assertThat(response.seniorId())
                .isEqualTo(senior.getSeniorId());
    }

    @Test
    @DisplayName("Account있는 경우 계정 수정 테스트")
    void updateSeniorMyPageUserAccountWithAccount() {
        SeniorMyPageUserAccountRequest request =
                new SeniorMyPageUserAccountRequest("a", "b", "a", "b", "a", "b");

        given(seniorGetService.byUserWithAll(user))
                .willReturn(senior);
        given(accountGetService.bySenior(senior))
                .willReturn(Optional.of(mock(Account.class)));
        given(encryptorUtils.encryptData(request.accountNumber()))
                .willReturn("encrypt");
        given(salaryGetService.allBySeniorAndAccountIsNull(senior))
                .willReturn(List.of(mock(Salary.class)));

        seniorManageUseCase.updateSeniorMyPageUserAccount(user, request);

        verify(userUpdateService)
                .updateSeniorUserAccount(user, request);
        verify(accountUpdateService)
                .updateAccount(any(Account.class), eq(request), eq("encrypt"));
    }

    @Test
    @DisplayName("번호 예외 테스트")
    void userAccountInvalidPhoneNumber() {
        SeniorMyPageUserAccountRequest request =
                new SeniorMyPageUserAccountRequest("a", "1234", "a", "b", "a", "b");

        doThrow(PhoneNumberException.class)
                .when(userUtils)
                .checkPhoneNumber(request.phoneNumber());

        assertThatThrownBy(() -> seniorManageUseCase.updateSeniorMyPageUserAccount(user, request))
                .isInstanceOf(PhoneNumberException.class);
    }
}
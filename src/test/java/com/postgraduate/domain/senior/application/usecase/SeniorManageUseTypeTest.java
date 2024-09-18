package com.postgraduate.domain.senior.application.usecase;

import com.postgraduate.domain.member.senior.application.dto.req.*;
import com.postgraduate.domain.member.senior.application.usecase.SeniorManageUseCase;
import com.postgraduate.domain.member.senior.domain.entity.*;
import com.postgraduate.domain.member.senior.domain.service.SeniorDeleteService;
import com.postgraduate.domain.member.senior.domain.service.SeniorSaveService;
import com.postgraduate.domain.salary.application.mapper.SalaryMapper;
import com.postgraduate.domain.salary.domain.entity.Salary;
import com.postgraduate.domain.salary.domain.service.SalaryGetService;
import com.postgraduate.domain.salary.domain.service.SalaryUpdateService;
import com.postgraduate.domain.member.senior.application.dto.res.SeniorProfileUpdateResponse;
import com.postgraduate.domain.member.senior.application.utils.SeniorUtils;
import com.postgraduate.domain.member.senior.domain.service.SeniorGetService;
import com.postgraduate.domain.member.senior.domain.service.SeniorUpdateService;
import com.postgraduate.domain.member.senior.exception.KeywordException;
import com.postgraduate.domain.member.user.application.utils.UserUtils;
import com.postgraduate.domain.member.user.domain.entity.User;
import com.postgraduate.domain.member.user.domain.service.UserUpdateService;
import com.postgraduate.domain.member.user.exception.PhoneNumberException;
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
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static com.postgraduate.domain.member.senior.domain.entity.constant.Status.APPROVE;
import static com.postgraduate.domain.member.user.domain.entity.constant.Role.SENIOR;
import static com.postgraduate.domain.member.user.domain.entity.constant.Role.USER;
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
    private SalaryGetService salaryGetService;
    @Mock
    private SeniorDeleteService seniorDeleteService;
    @Mock
    private SalaryUpdateService salaryUpdateService;
    @Mock
    private SeniorSaveService seniorSaveService;
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
        Available available1 = mock(Available.class);
        Available available2 = mock(Available.class);
        Available available3 = mock(Available.class);
        List<Available> availables = List.of(available1, available2, available3);
        info = new Info("a", "a", "a", "a", "a", "a", TRUE, TRUE, "a", "chatLink", 30);
        profile = new Profile("a", "a", "a");
        user = new User(1L, 1234L, "a",
                "a", "123", "a",
                1, USER, TRUE, LocalDateTime.now(), LocalDateTime.now(), TRUE, TRUE, null);
        senior = new Senior(1L, user, "a",
                APPROVE, 1, 1, info, profile,
                LocalDateTime.now(), LocalDateTime.now(), availables, null);
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
        verify(seniorSaveService, times(1))
                .saveAllAvailable(any(), any());
    }

    @Test
    @DisplayName("Account없는 경우 계정 수정 테스트")
    void updateSeniorMyPageUserAccountWithNonAccount() {
        SeniorMyPageUserAccountRequest request =
                new SeniorMyPageUserAccountRequest("a", "b", "a", "b", "a", "b");

        given(seniorGetService.byUserWithAll(user))
                .willReturn(senior);
        given(encryptorUtils.encryptData(request.accountNumber()))
                .willReturn("encrypt");
        given(salaryGetService.allBySeniorAndAccountIsNull(senior))
                .willReturn(List.of(mock(Salary.class)));

        seniorManageUseCase.updateSeniorMyPageUserAccount(user, request);

        verify(userUpdateService)
                .updateSeniorUserAccount(user, request);
    }

    @Test
    @DisplayName("계좌 생성 테스트")
    void saveAccount() {
        SeniorAccountRequest request = new SeniorAccountRequest("a", "a", "a");
        given(seniorGetService.byUser(user))
                .willReturn(senior);

        seniorManageUseCase.saveAccount(user, request);
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
        given(encryptorUtils.encryptData(request.accountNumber()))
                .willReturn("encrypt");
        given(salaryGetService.allBySeniorAndAccountIsNull(senior))
                .willReturn(List.of(mock(Salary.class)));

        seniorManageUseCase.updateSeniorMyPageUserAccount(user, request);

        verify(userUpdateService)
                .updateSeniorUserAccount(user, request);
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
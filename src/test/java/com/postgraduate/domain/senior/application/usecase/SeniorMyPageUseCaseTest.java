package com.postgraduate.domain.senior.application.usecase;

import com.postgraduate.domain.account.domain.entity.Account;
import com.postgraduate.domain.account.domain.service.AccountGetService;
import com.postgraduate.domain.available.domain.entity.Available;
import com.postgraduate.domain.available.domain.service.AvailableGetService;
import com.postgraduate.domain.senior.application.dto.res.SeniorMyPageProfileResponse;
import com.postgraduate.domain.senior.application.dto.res.SeniorMyPageResponse;
import com.postgraduate.domain.senior.application.dto.res.SeniorMyPageUserAccountResponse;
import com.postgraduate.domain.senior.domain.entity.Info;
import com.postgraduate.domain.senior.domain.entity.Profile;
import com.postgraduate.domain.senior.domain.entity.Senior;
import com.postgraduate.domain.senior.domain.service.SeniorGetService;
import com.postgraduate.domain.user.domain.entity.User;
import com.postgraduate.global.config.security.util.EncryptorUtils;
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
import static com.postgraduate.domain.senior.domain.entity.constant.Status.WAITING;
import static com.postgraduate.domain.user.domain.entity.constant.Role.USER;
import static java.lang.Boolean.TRUE;
import static java.time.LocalDate.now;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;

@ExtendWith(MockitoExtension.class)
class SeniorMyPageUseCaseTest {
    @Mock
    private SeniorGetService seniorGetService;
    @Mock
    private AvailableGetService availableGetService;
    @Mock
    private AccountGetService accountGetService;
    @Mock
    private EncryptorUtils encryptorUtils;
    @InjectMocks
    private SeniorMyPageUseCase seniorMyPageUseCase;

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
    @DisplayName("Profile Null 선배 자신의 정보 조회")
    void getSeniorInfoWithNullProfile() {
        senior = new Senior(1L, user, "a", WAITING, 1, info, null, LocalDateTime.now(), LocalDateTime.now());

        given(seniorGetService.byUser(user))
                .willReturn(senior);

        SeniorMyPageResponse seniorInfo = seniorMyPageUseCase.getSeniorInfo(user);

        assertThat(seniorInfo.profileRegister())
                .isFalse();
    }

    @Test
    @DisplayName("Profile 존재 선배 자신의 정보 조회")
    void getSeniorInfoWithProfile() {
        given(seniorGetService.byUser(user))
                .willReturn(senior);

        SeniorMyPageResponse seniorInfo = seniorMyPageUseCase.getSeniorInfo(user);

        assertThat(seniorInfo.profileRegister())
                .isTrue();
    }

    @Test
    @DisplayName("선배 자신의 마이페이지 조회")
    void getSeniorMyPageProfile() {
        Available available1 = new Available(1L, "월", "12:00", "18:00", senior);
        Available available2 = new Available(2L, "화", "12:00", "18:00", senior);
        Available available3 = new Available(3L, "수", "12:00", "18:00", senior);
        List<Available> availables = List.of(available1, available2, available3);

        given(seniorGetService.byUser(user))
                .willReturn(senior);
        given(availableGetService.bySenior(senior.getSeniorId()))
                .willReturn(availables);

        SeniorMyPageProfileResponse myPageProfile = seniorMyPageUseCase.getSeniorMyPageProfile(user);

        assertThat(myPageProfile.times().size())
                .isEqualTo(availables.size());
    }

    @Test
    @DisplayName("Account없는 경우 계정 설정 조회")
    void getSeniorMyPageUserAccountWithNull() {
        given(seniorGetService.byUser(user))
                .willReturn(senior);
        given(accountGetService.bySenior(senior))
                .willReturn(Optional.ofNullable(null));

        SeniorMyPageUserAccountResponse seniorMyPageUserAccount = seniorMyPageUseCase.getSeniorMyPageUserAccount(user);

        assertThat(seniorMyPageUserAccount.accountHolder())
                .isNull();
        assertThat(seniorMyPageUserAccount.accountNumber())
                .isNull();
        assertThat(seniorMyPageUserAccount.bank())
                .isNull();
        assertThat(seniorMyPageUserAccount.nickName())
                .isEqualTo(user.getNickName());
    }

    @Test
    @DisplayName("Account있는 경우 계정 설정 조회")
    void getSeniorMyPageUserAccountWithAccount() {
        Account account = new Account(1L, "123", "a", "a", senior);

        given(seniorGetService.byUser(user))
                .willReturn(senior);
        given(accountGetService.bySenior(senior))
                .willReturn(Optional.of(account));
        given(encryptorUtils.decryptData(account.getAccountNumber()))
                .willReturn(account.getAccountNumber());

        SeniorMyPageUserAccountResponse seniorMyPageUserAccount = seniorMyPageUseCase.getSeniorMyPageUserAccount(user);

        assertThat(seniorMyPageUserAccount.accountHolder())
                .isEqualTo(account.getAccountHolder());
        assertThat(seniorMyPageUserAccount.accountNumber())
                .isEqualTo(account.getAccountNumber());
        assertThat(seniorMyPageUserAccount.bank())
                .isEqualTo(account.getBank());
        assertThat(seniorMyPageUserAccount.nickName())
                .isEqualTo(user.getNickName());
    }
}
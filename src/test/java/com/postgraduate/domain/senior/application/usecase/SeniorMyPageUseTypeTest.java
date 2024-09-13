package com.postgraduate.domain.senior.application.usecase;

import com.postgraduate.domain.member.senior.application.dto.res.SeniorMyPageProfileResponse;
import com.postgraduate.domain.member.senior.application.dto.res.SeniorMyPageResponse;
import com.postgraduate.domain.member.senior.application.dto.res.SeniorMyPageUserAccountResponse;
import com.postgraduate.domain.member.senior.application.dto.res.SeniorPossibleResponse;
import com.postgraduate.domain.member.senior.application.usecase.SeniorMyPageUseCase;
import com.postgraduate.domain.member.senior.domain.entity.*;
import com.postgraduate.domain.member.senior.domain.service.SeniorGetService;
import com.postgraduate.domain.member.user.domain.entity.User;
import com.postgraduate.global.config.security.util.EncryptorUtils;
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

import static com.postgraduate.domain.member.senior.domain.entity.constant.Status.APPROVE;
import static com.postgraduate.domain.member.senior.domain.entity.constant.Status.WAITING;
import static com.postgraduate.domain.member.user.domain.entity.constant.Role.USER;
import static java.lang.Boolean.FALSE;
import static java.lang.Boolean.TRUE;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mock;

@ExtendWith(MockitoExtension.class)
class SeniorMyPageUseTypeTest {
    @Mock
    private SeniorGetService seniorGetService;
    @Mock
    private EncryptorUtils encryptorUtils;
    @InjectMocks
    private SeniorMyPageUseCase seniorMyPageUseCase;

    private User user;
    private Senior senior;
    private Info info;
    private Profile profile;
    private List<Available> availables;

    @BeforeEach
    void setting() {
        Available available1 = mock(Available.class);
        Available available2 = mock(Available.class);
        Available available3 = mock(Available.class);
        availables = List.of(available1, available2, available3);
        info = new Info("a", "a", "a", "a", "a", "a", TRUE, TRUE, "a", "chatLink", 30);
        profile = new Profile("a", "a", "a");
        user = new User(1L, 1234L, "a",
                "a", "123", "a",
                1, USER, TRUE, LocalDateTime.now(), LocalDateTime.now(), TRUE, TRUE, null);
        senior = new Senior(1L, user, "a",
                APPROVE, 1, 1, info, profile,
                LocalDateTime.now(), LocalDateTime.now(), availables, new Account());
    }

    @Test
    @DisplayName("Profile Null 선배 자신의 정보 조회")
    void getSeniorInfoWithNullProfile() {
        senior = new Senior(1L, user, "a", WAITING, 1, 1, info, null, LocalDateTime.now(), LocalDateTime.now(), new ArrayList<>(), null);

        given(seniorGetService.byUser(user))
                .willReturn(senior);

        SeniorMyPageResponse seniorInfo = seniorMyPageUseCase.getSeniorMyPage(user);

        assertThat(seniorInfo.profileRegister())
                .isFalse();
    }

    @Test
    @DisplayName("Profile 존재 선배 자신의 정보 조회")
    void getSeniorInfoWithProfile() {
        given(seniorGetService.byUser(user))
                .willReturn(senior);

        SeniorMyPageResponse seniorInfo = seniorMyPageUseCase.getSeniorMyPage(user);

        assertThat(seniorInfo.profileRegister())
                .isTrue();
    }

//    @Test
//    @DisplayName("선배 자신의 마이페이지 조회")
//    void getSeniorMyPageProfile() {
//        given(seniorGetService.byUser(user))
//                .willReturn(senior);
//
//        SeniorMyPageProfileResponse myPageProfile = seniorMyPageUseCase.getSeniorMyPageProfile(user);
//
//        assertThat(myPageProfile.times())
//                .hasSameSizeAs(availables);
//    }

    @Test
    @DisplayName("선배 자신의 마이페이지 프로필 작성 이전 Info조회 테스트")
    void getSeniorMyPageProfileWithNull() {
        Senior nullSenior = new Senior(-2L, user, "asd", APPROVE, 1, 1, info, null, LocalDateTime.now(), LocalDateTime.now(), null, null);
        given(seniorGetService.byUser(user))
                .willReturn(nullSenior);

        SeniorMyPageProfileResponse myPageProfile = seniorMyPageUseCase.getSeniorMyPageProfile(user);

        assertThat(myPageProfile.lab())
                .isNotEmpty();
        assertThat(myPageProfile.field())
                .isNotEmpty();
        assertThat(myPageProfile.keyword())
                .isNotEmpty();
        assertThat(myPageProfile.chatLink())
                .isNotEmpty();
        assertThat(myPageProfile.info())
                .isNull();
        assertThat(myPageProfile.target())
                .isNull();
        assertThat(myPageProfile.oneLiner())
                .isNull();
        assertThat(myPageProfile.times())
                .isNull();
    }

    @Test
    @DisplayName("Account없는 경우 계정 설정 조회")
    void getSeniorMyPageUserAccountWithNull() {
        given(seniorGetService.byUser(user))
                .willReturn(senior);

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

//    @Test
//    @DisplayName("Account있는 경우 계정 설정 조회")
//    void getSeniorMyPageUserAccountWithAccount() {
//        Account account = new Account(1L, "123", "a", "a", senior);
//        given(seniorGetService.byUser(user))
//                .willReturn(senior);
//        given(encryptorUtils.decryptData(account.getAccountNumber()))
//                .willReturn(account.getAccountNumber());
//
//        SeniorMyPageUserAccountResponse seniorMyPageUserAccount = seniorMyPageUseCase.getSeniorMyPageUserAccount(user);
//
//        assertThat(seniorMyPageUserAccount.accountHolder())
//                .isEqualTo(account.getAccountHolder());
//        assertThat(seniorMyPageUserAccount.accountNumber())
//                .isEqualTo(account.getAccountNumber());
//        assertThat(seniorMyPageUserAccount.bank())
//                .isEqualTo(account.getBank());
//        assertThat(seniorMyPageUserAccount.nickName())
//                .isEqualTo(user.getNickName());
//    }

//    @Test
//    @DisplayName("후배 가입 확인")
//    void checkUser() {
//        given(user.isJunior())
//                .willReturn(TRUE);
//        SeniorPossibleResponse response = seniorMyPageUseCase.checkUser(user);
//
//        assertThat(response.possible())
//                .isEqualTo(TRUE);
//        assertThat(response.socialId())
//                .isEqualTo(user.getSocialId());
//    }

    @Test
    @DisplayName("후배 미가입 확인")
    void checkUserWithNull() {
        given(user.isJunior())
                .willReturn(FALSE);
        SeniorPossibleResponse response = seniorMyPageUseCase.checkUser(user);

        assertThat(response.possible())
                .isEqualTo(FALSE);
        assertThat(response.socialId())
                .isEqualTo(user.getSocialId());
    }
}
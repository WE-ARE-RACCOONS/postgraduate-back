package com.postgraduate.domain.senior.application.usecase;

import com.postgraduate.domain.senior.application.dto.res.SeniorInfoResponse;
import com.postgraduate.domain.senior.application.dto.res.SeniorProfileResponse;
import com.postgraduate.domain.senior.application.mapper.SeniorMapper;
import com.postgraduate.domain.senior.domain.entity.Account;
import com.postgraduate.domain.senior.domain.entity.Info;
import com.postgraduate.domain.senior.domain.entity.Profile;
import com.postgraduate.domain.senior.domain.entity.Senior;
import com.postgraduate.domain.senior.domain.entity.constant.Status;
import com.postgraduate.domain.senior.domain.service.SeniorGetService;
import com.postgraduate.domain.user.domain.entity.Hope;
import com.postgraduate.domain.user.domain.entity.User;
import com.postgraduate.domain.user.domain.entity.constant.Role;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;

@ExtendWith(MockitoExtension.class)
public class SeniorMyPageUseCaseTest {
    @Mock
    private SeniorGetService seniorGetService;
    @InjectMocks
    private SeniorMyPageUseCase seniorMyPageUseCase;
    private Senior testSenior;
    private User testUser;
    @BeforeEach
    void setSenior() {
        Hope hope = new Hope("computer","ai", true);
        testUser = new User(100000000L, 12345L, "test.com",
                "test", "test.png", "01012341234", 0, Role.USER, hope,
                LocalDate.now(), LocalDate.now());
        Account account = new Account("account", "bank", "123");
        Profile profile = new Profile("info", "hello", "keyword", "you", "abc", "1000", 10);
        Info info = new Info("c", "m", "p", "p", "a", "f");
        testSenior = Senior.builder()
                .seniorId(100000000L)
                .certification("certification")
                .hit(0)
                .account(account)
                .profile(profile)
                .info(info)
                .status(Status.APPROVE)
                .user(testUser)
                .build();
    }

    @Test
    @DisplayName("선배 기본 정보 조회")
    void seniorInfo() {
        given(seniorGetService.byUser(testUser))
                .willReturn(testSenior);

        SeniorInfoResponse expect = SeniorMapper.mapToSeniorInfo(testSenior, testSenior.getStatus(), true);
        SeniorInfoResponse actual = seniorMyPageUseCase.seniorInfo(testUser);

        assertThat(actual.getNickName())
                .isEqualTo(expect.getNickName());
        assertThat(actual.getProfile())
                .isEqualTo(expect.getProfile());
        assertThat(actual.getCertificationRegister())
                .isEqualTo(expect.getCertificationRegister());
    }

    @Test
    @DisplayName("선배 프로필 조회")
    void seniorProfile() {
        given(seniorGetService.byUser(testUser))
                .willReturn(testSenior);

        SeniorProfileResponse expect = SeniorMapper.mapToSeniorProfileInfo(testSenior);
        SeniorProfileResponse actual = seniorMyPageUseCase.getSeniorProfile(testUser);

        assertThat(actual.getProfile())
                .isEqualTo(expect.getProfile());
        assertThat(actual.getBank())
                .isEqualTo(expect.getBank());
        assertThat(actual.getAccount())
                .isEqualTo(expect.getAccount());
    }
}

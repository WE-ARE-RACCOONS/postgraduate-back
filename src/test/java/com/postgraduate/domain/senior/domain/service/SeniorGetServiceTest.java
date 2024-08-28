package com.postgraduate.domain.senior.domain.service;

import com.postgraduate.domain.senior.domain.entity.Senior;
import com.postgraduate.domain.senior.domain.repository.SeniorRepository;
import com.postgraduate.domain.senior.exception.NoneSeniorException;
import com.postgraduate.domain.user.domain.entity.User;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static java.lang.Boolean.FALSE;
import static java.util.Optional.of;
import static java.util.Optional.ofNullable;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mock;

@ExtendWith(MockitoExtension.class)
class SeniorGetServiceTest {
    @Mock
    private SeniorRepository seniorRepository;
    @InjectMocks
    private SeniorGetService seniorGetService;

    private User user = mock(User.class);
    private Senior senior = mock(Senior.class);
    private Long seniorId = -1L;
    private String nickName = "nick";

    @Test
    @DisplayName("User 기반 Senior 조회 예외 테스트")
    void byUserFail() {
        given(seniorRepository.findByUserAndUser_IsDeleteIsFalse(user))
                .willReturn(ofNullable(null));

        assertThatThrownBy(() -> seniorGetService.byUser(user))
                .isInstanceOf(NoneSeniorException.class);
    }

    @Test
    @DisplayName("User 기반 Senior 조회 테스트")
    void byUser() {
        given(seniorRepository.findByUserAndUser_IsDeleteIsFalse(user))
                .willReturn(of(senior));

        assertThat(seniorGetService.byUser(user))
                .isEqualTo(senior);
    }

    @Test
    @DisplayName("User 기반 Senior 조회 예외 테스트")
    void byUserWithAllFail() {
        given(seniorRepository.findByUserWithAll(user))
                .willReturn(ofNullable(null));

        assertThatThrownBy(() -> seniorGetService.byUserWithAll(user))
                .isInstanceOf(NoneSeniorException.class);
    }

    @Test
    @DisplayName("User 기반 Senior 조회 테스트")
    void byUserWithAll() {
        given(seniorRepository.findByUserWithAll(user))
                .willReturn(of(senior));

        assertThat(seniorGetService.byUserWithAll(user))
                .isEqualTo(senior);
    }

    @Test
    @DisplayName("SeniorId 기반 Senior 조회 예외 테스트")
    void bySeniorIdFail() {
        given(seniorRepository.findBySeniorId(seniorId))
                .willReturn(ofNullable(null));

        assertThatThrownBy(() -> seniorGetService.bySeniorId(seniorId))
                .isInstanceOf(NoneSeniorException.class);
    }

    @Test
    @DisplayName("SeniorId 기반 Senior 조회 테스트")
    void bySeniorId() {
        given(seniorRepository.findBySeniorId(seniorId))
                .willReturn(of(senior));

        assertThat(seniorGetService.bySeniorId(seniorId))
                .isEqualTo(senior);
    }

    @Test
    @DisplayName("nickName 기반 Senior 조회 예외 테스트")
    void bySeniorNickNameFail() {
        given(seniorRepository.findByUser_NickNameAndUser_IsDelete(nickName, FALSE))
                .willReturn(ofNullable(null));

        assertThatThrownBy(() -> seniorGetService.bySeniorNickName(nickName))
                .isInstanceOf(NoneSeniorException.class);
    }

    @Test
    @DisplayName("nickName 기반 Senior 조회 테스트")
    void bySeniorNickName() {
        given(seniorRepository.findByUser_NickNameAndUser_IsDelete(nickName, FALSE))
                .willReturn(of(senior));

        assertThat(seniorGetService.bySeniorNickName(nickName))
                .isEqualTo(senior);
    }
}
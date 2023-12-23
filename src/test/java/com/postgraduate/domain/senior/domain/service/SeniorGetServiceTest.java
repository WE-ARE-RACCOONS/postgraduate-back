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

import static com.postgraduate.domain.senior.domain.entity.constant.Status.APPROVE;
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

    @Test
    @DisplayName("User 기반 Senior 조회 예외 테스트")
    void byUserFail() {
        User user = mock(User.class);
        given(seniorRepository.findByUser(user))
                .willReturn(ofNullable(null));

        assertThatThrownBy(() -> seniorGetService.byUser(user))
                .isInstanceOf(NoneSeniorException.class);
    }

    @Test
    @DisplayName("User 기반 Senior 조회 테스트")
    void byUser() {
        User user = mock(User.class);
        Senior senior = mock(Senior.class);
        given(seniorRepository.findByUser(user))
                .willReturn(of(senior));

        assertThat(seniorGetService.byUser(user))
                .isEqualTo(senior);
    }

    @Test
    @DisplayName("SeniorId 기반 Senior 조회 예외 테스트")
    void bySeniorIdFail() {
        long seniorId = 1L;
        given(seniorRepository.findBySeniorIdAndUser_IsDelete(seniorId, FALSE))
                .willReturn(ofNullable(null));

        assertThatThrownBy(() -> seniorGetService.bySeniorId(seniorId))
                .isInstanceOf(NoneSeniorException.class);
    }

    @Test
    @DisplayName("SeniorId 기반 Senior 조회 테스트")
    void bySeniorId() {
        Senior senior = mock(Senior.class);
        long seniorId = 1L;
        given(seniorRepository.findBySeniorIdAndUser_IsDelete(seniorId, FALSE))
                .willReturn(of(senior));

        assertThat(seniorGetService.bySeniorId(seniorId))
                .isEqualTo(senior);
    }

    @Test
    @DisplayName("Certification 기반 Senior 조회 예외 테스트")
    void byCertificationFail() {
        long seniorId = 1L;
        given(seniorRepository.findBySeniorIdAndProfileNotNullAndStatusAndUser_IsDelete(seniorId, APPROVE, FALSE))
                .willReturn(ofNullable(null));

        assertThatThrownBy(() -> seniorGetService.bySeniorIdWithCertification(seniorId))
                .isInstanceOf(NoneSeniorException.class);
    }

    @Test
    @DisplayName("Certification 기반 Senior 조회 테스트")
    void byCertification() {
        Senior senior = mock(Senior.class);
        long seniorId = 1L;
        given(seniorRepository.findBySeniorIdAndProfileNotNullAndStatusAndUser_IsDelete(seniorId, APPROVE, FALSE))
                .willReturn(of(senior));

        assertThat(seniorGetService.bySeniorIdWithCertification(seniorId))
                .isEqualTo(senior);
    }
}
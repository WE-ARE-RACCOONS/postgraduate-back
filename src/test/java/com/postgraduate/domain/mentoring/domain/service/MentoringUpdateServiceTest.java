package com.postgraduate.domain.mentoring.domain.service;

import com.postgraduate.domain.mentoring.domain.entity.Mentoring;
import com.postgraduate.domain.payment.domain.entity.Payment;
import com.postgraduate.domain.senior.domain.entity.Senior;
import com.postgraduate.domain.user.domain.entity.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;

import static com.postgraduate.domain.mentoring.domain.entity.constant.Status.*;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;

@ExtendWith(MockitoExtension.class)
class MentoringUpdateServiceTest {
    @InjectMocks
    private MentoringUpdateService mentoringUpdateService;

    private Mentoring mentoring;
    private
    @BeforeEach
    void setting() {
        User user = mock(User.class);
        Senior senior = mock(Senior.class);
        Payment payment = mock(Payment.class);

        mentoring = new Mentoring(1L, user, senior, payment, "a", "a", "a", 1
        , WAITING, LocalDateTime.now(), LocalDateTime.now());
    }

    @Test
    @DisplayName("DONE으로 변경")
    void updateStatusDone() {
        mentoringUpdateService.updateStatus(mentoring, DONE);
        assertThat(mentoring.getStatus())
                .isEqualTo(DONE);
    }

    @Test
    @DisplayName("CANCEL 변경")
    void updateStatusCancel() {
        mentoringUpdateService.updateStatus(mentoring, CANCEL);
        assertThat(mentoring.getStatus())
                .isEqualTo(CANCEL);
    }

    @Test
    @DisplayName("EXPECTED 변경")
    void updateStatusExpected() {
        mentoringUpdateService.updateStatus(mentoring, EXPECTED);
        assertThat(mentoring.getStatus())
                .isEqualTo(EXPECTED);
    }

    @Test
    @DisplayName("REFUSE 변경")
    void updateStatusRefuse() {
        mentoringUpdateService.updateStatus(mentoring, REFUSE);
        assertThat(mentoring.getStatus())
                .isEqualTo(REFUSE);
    }

    @Test
    @DisplayName("date 업데이트 확인")
    void updateDate() {
        mentoringUpdateService.updateDate(mentoring, "update");
        assertThat(mentoring.getDate())
                .isEqualTo("update");
    }
}
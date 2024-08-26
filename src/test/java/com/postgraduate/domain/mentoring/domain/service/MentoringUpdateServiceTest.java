package com.postgraduate.domain.mentoring.domain.service;

import com.postgraduate.domain.mentoring.domain.entity.Mentoring;
import com.postgraduate.domain.payment.domain.entity.Payment;
import com.postgraduate.domain.senior.salary.domain.entity.Salary;
import com.postgraduate.domain.senior.domain.entity.Senior;
import com.postgraduate.domain.user.user.domain.entity.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;

import static com.postgraduate.domain.mentoring.domain.entity.constant.MentoringStatus.*;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;

@ExtendWith(MockitoExtension.class)
class MentoringUpdateServiceTest {
    @InjectMocks
    private MentoringUpdateService mentoringUpdateService;

    private Mentoring mentoring;
    private Salary salary;
    private
    @BeforeEach
    void setting() {
        User user = mock(User.class);
        Senior senior = mock(Senior.class);
        Payment payment = mock(Payment.class);
        salary = mock(Salary.class);

        mentoring = new Mentoring(1L, user, senior, payment, null, "a", "a", "a", 1
        , WAITING, LocalDateTime.now(), LocalDateTime.now());
    }

    @Test
    @DisplayName("DONE으로 변경")
    void updateStatusDone() {
        mentoringUpdateService.updateDone(mentoring, salary);
        assertThat(mentoring.getMentoringStatus())
                .isEqualTo(DONE);
    }

    @Test
    @DisplayName("CANCEL 변경")
    void updateStatusCancel() {
        mentoringUpdateService.updateCancel(mentoring);
        assertThat(mentoring.getMentoringStatus())
                .isEqualTo(CANCEL);
    }

    @Test
    @DisplayName("EXPECTED 변경")
    void updateStatusExpected() {
        mentoringUpdateService.updateExpected(mentoring, "update");
        assertThat(mentoring.getMentoringStatus())
                .isEqualTo(EXPECTED);
        assertThat(mentoring.getDate())
                .isEqualTo("update");
    }

    @Test
    @DisplayName("REFUSE 변경")
    void updateStatusRefuse() {
        mentoringUpdateService.updateRefuse(mentoring);
        assertThat(mentoring.getMentoringStatus())
                .isEqualTo(REFUSE);
    }
}
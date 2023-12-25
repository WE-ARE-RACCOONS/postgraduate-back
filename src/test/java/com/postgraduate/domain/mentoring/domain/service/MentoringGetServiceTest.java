package com.postgraduate.domain.mentoring.domain.service;

import com.postgraduate.domain.mentoring.domain.entity.Mentoring;
import com.postgraduate.domain.mentoring.domain.repository.MentoringRepository;
import com.postgraduate.domain.mentoring.exception.MentoringNotFoundException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static java.lang.Boolean.FALSE;
import static java.util.Optional.of;
import static java.util.Optional.ofNullable;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mock;

@ExtendWith(MockitoExtension.class)
class MentoringGetServiceTest {
    @Mock
    private MentoringRepository mentoringRepository;
    @InjectMocks
    private MentoringGetService mentoringGetService;

    @Test
    @DisplayName("mentoringId를 통해 조회 예외 테스트")
    void byMentoringIdFail() {
        long mentoringId = 1L;
        given(mentoringRepository.findByMentoringIdAndUser_IsDeleteAndSenior_User_IsDelete(mentoringId, FALSE, FALSE))
                .willReturn(ofNullable(null));

        assertThatThrownBy(() -> mentoringGetService.byMentoringId(mentoringId))
                .isInstanceOf(MentoringNotFoundException.class);
    }

    @Test
    @DisplayName("mentoringId를 통해 조회 테스트")
    void byMentoringId() {
        long mentoringId = 1L;
        Mentoring mentoring = mock(Mentoring.class);
        given(mentoringRepository.findByMentoringIdAndUser_IsDeleteAndSenior_User_IsDelete(mentoringId, FALSE, FALSE))
                .willReturn(of(mentoring));

        assertThat(mentoringGetService.byMentoringId(mentoringId))
                .isEqualTo(mentoring);
    }
}
package com.postgraduate.domain.mentoring.application.usecase;

import com.postgraduate.domain.mentoring.application.dto.res.AppliedMentoringResponse;
import com.postgraduate.domain.mentoring.domain.entity.Mentoring;
import com.postgraduate.domain.mentoring.domain.service.MentoringGetService;
import com.postgraduate.domain.mentoring.exception.MentoringDoneException;
import com.postgraduate.domain.senior.domain.entity.Info;
import com.postgraduate.domain.senior.domain.entity.Profile;
import com.postgraduate.domain.senior.domain.entity.Senior;
import com.postgraduate.domain.user.domain.entity.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static com.postgraduate.domain.mentoring.domain.entity.constant.Status.*;
import static com.postgraduate.domain.senior.domain.entity.constant.Status.APPROVE;
import static com.postgraduate.domain.user.domain.entity.constant.Role.USER;
import static java.lang.Boolean.TRUE;
import static java.time.LocalDate.now;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.BDDMockito.given;

@ExtendWith(MockitoExtension.class)
class MentoringUserInfoUseCaseTest {
    @Mock
    private MentoringGetService mentoringGetService;

    @Mock
    private CheckIsMyMentoringUseCase checkIsMyMentoringUseCase;

    @InjectMocks
    private MentoringUserInfoUseCase mentoringUserInfoUseCase;

    private Long mentoringId = 1L;
    private User user;
    private Senior senior;
    private Info info;
    private Profile profile;
    private Mentoring mentoring;

    @BeforeEach
    void setting() {
        info = new Info("a", "a", "a", "a", "a", "a", TRUE, TRUE, "a");
        profile = new Profile("a", "a", "a", "a", 40);
        user = new User(1L, 1234L, "a",
                "a", "123", "a",
                1, USER, TRUE, now(), now(), TRUE);
        senior = new Senior(1L, user, "a",
                APPROVE, 1, info, profile,
                now(), now());
    }

    @Test
    @DisplayName("Detail 반환 테스트")
    void getMentoringDetail() {
        mentoring = new Mentoring(mentoringId, user, senior
                , "a", "b", "c"
                , 40, 40, EXPECTED
                , now(), now());

        given(checkIsMyMentoringUseCase.byUser(user, mentoringId))
                .willReturn(mentoring);

        assertThat(mentoringUserInfoUseCase.getMentoringDetail(user, mentoringId))
                .isNotNull();
    }

    @Test
    @DisplayName("Detail 반환 실패 테스트")
    void getMentoringDetailFail() {
        mentoring = new Mentoring(mentoringId, user, senior
                , "a", "b", "c"
                , 40, 40, DONE
                , now(), now());

        given(checkIsMyMentoringUseCase.byUser(user, mentoringId))
                .willReturn(mentoring);

        assertThatThrownBy(() -> mentoringUserInfoUseCase.getMentoringDetail(user, mentoringId))
                .isInstanceOf(MentoringDoneException.class);
    }

    @Test
    @DisplayName("WAITING 반환 테스트")
    void getWaiting() {
        Mentoring mentoring1 = new Mentoring(1L, user, senior
                , "a", "b", "c"
                , 40, 40, WAITING
                , now(), now());

        Mentoring mentoring2 = new Mentoring(2L, user, senior
                , "a", "b", "c"
                , 40, 40, WAITING
                , now(), now());

        Mentoring mentoring3 = new Mentoring(3L, user, senior
                , "a", "b", "c"
                , 40, 40, WAITING
                , now(), now());
        List<Mentoring> mentorings = List.of(mentoring1, mentoring2, mentoring3);

        given(mentoringGetService.mentoringByUser(user, WAITING))
                .willReturn(mentorings);

        AppliedMentoringResponse waiting = mentoringUserInfoUseCase.getWaiting(user);

        assertThat(waiting.mentoringInfos().size())
                .isEqualTo(mentorings.size());
    }

    @Test
    @DisplayName("EXPECTED 반환 테스트")
    void getExpected() {
        Mentoring mentoring1 = new Mentoring(1L, user, senior
                , "a", "b", "c"
                , 40, 40, EXPECTED
                , now(), now());

        Mentoring mentoring2 = new Mentoring(2L, user, senior
                , "a", "b", "c"
                , 40, 40, EXPECTED
                , now(), now());

        Mentoring mentoring3 = new Mentoring(3L, user, senior
                , "a", "b", "c"
                , 40, 40, EXPECTED
                , now(), now());
        List<Mentoring> mentorings = List.of(mentoring1, mentoring2, mentoring3);

        given(mentoringGetService.mentoringByUser(user, EXPECTED))
                .willReturn(mentorings);

        AppliedMentoringResponse expected = mentoringUserInfoUseCase.getExpected(user);

        assertThat(expected.mentoringInfos().size())
                .isEqualTo(mentorings.size());
    }

    @Test
    @DisplayName("DONE 반환 테스트")
    void getDone() {
        Mentoring mentoring1 = new Mentoring(1L, user, senior
                , "a", "b", "c"
                , 40, 40, DONE
                , now(), now());

        Mentoring mentoring2 = new Mentoring(2L, user, senior
                , "a", "b", "c"
                , 40, 40, DONE
                , now(), now());

        Mentoring mentoring3 = new Mentoring(3L, user, senior
                , "a", "b", "c"
                , 40, 40, DONE
                , now(), now());
        List<Mentoring> mentorings = List.of(mentoring1, mentoring2, mentoring3);

        given(mentoringGetService.mentoringByUser(user, DONE))
                .willReturn(mentorings);

        AppliedMentoringResponse done = mentoringUserInfoUseCase.getDone(user);

        assertThat(done.mentoringInfos().size())
                .isEqualTo(mentorings.size());
    }
}
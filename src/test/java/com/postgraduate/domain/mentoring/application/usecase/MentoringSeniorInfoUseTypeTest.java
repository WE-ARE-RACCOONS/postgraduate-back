package com.postgraduate.domain.mentoring.application.usecase;

import com.postgraduate.domain.mentoring.application.dto.DoneSeniorMentoringInfo;
import com.postgraduate.domain.mentoring.application.dto.res.DoneSeniorMentoringResponse;
import com.postgraduate.domain.mentoring.application.dto.res.ExpectedSeniorMentoringResponse;
import com.postgraduate.domain.mentoring.application.dto.res.SeniorMentoringDetailResponse;
import com.postgraduate.domain.mentoring.application.dto.res.WaitingSeniorMentoringResponse;
import com.postgraduate.domain.mentoring.application.mapper.MentoringMapper;
import com.postgraduate.domain.mentoring.domain.entity.Mentoring;
import com.postgraduate.domain.mentoring.domain.service.MentoringGetService;
import com.postgraduate.domain.mentoring.exception.MentoringNotFoundException;
import com.postgraduate.domain.payment.domain.entity.Payment;
import com.postgraduate.domain.payment.domain.entity.constant.Status;
import com.postgraduate.domain.salary.domain.entity.Salary;
import com.postgraduate.domain.member.senior.domain.entity.Info;
import com.postgraduate.domain.member.senior.domain.entity.Profile;
import com.postgraduate.domain.member.senior.domain.entity.Senior;
import com.postgraduate.domain.member.senior.domain.service.SeniorGetService;
import com.postgraduate.domain.member.user.domain.entity.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

import static com.postgraduate.domain.mentoring.domain.entity.constant.MentoringStatus.WAITING;
import static com.postgraduate.domain.member.senior.domain.entity.constant.Status.APPROVE;
import static com.postgraduate.domain.member.user.domain.entity.constant.Role.SENIOR;
import static com.postgraduate.domain.member.user.domain.entity.constant.Role.USER;
import static java.lang.Boolean.FALSE;
import static java.lang.Boolean.TRUE;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.mock;

@ExtendWith(MockitoExtension.class)
class MentoringSeniorInfoUseTypeTest {
    @Mock
    private MentoringGetService mentoringGetService;
    @Mock
    private SeniorGetService seniorGetService;
    @Mock
    private MentoringMapper mentoringMapper;
    @InjectMocks
    private MentoringSeniorInfoUseCase mentoringSeniorInfoUseCase;

    private Long mentoringId = -1L;
    private User user;
    private Senior senior;
    private Info info;
    private Profile profile;
    private Salary salary;
    private Payment payment;
    private User mentoringUser;
    private Mentoring mentoring;

    @BeforeEach
    void setting() {
        info = new Info("a", "a", "a", "a", "a", "a", TRUE, TRUE, "a", "chatLink", 30);
        profile = new Profile("a", "a", "a");
        user = new User(-1L, 1234L, "a",
                "a", "123", "a",
                0, SENIOR, TRUE, LocalDateTime.now(), LocalDateTime.now(), TRUE, TRUE);
        mentoringUser = new User(-2L, 12345L, "a",
                "a", "123", "a",
                0, USER, TRUE, LocalDateTime.now(), LocalDateTime.now(), TRUE, TRUE);
        senior = new Senior(-1L, user, "a",
                APPROVE, 1,1, info, profile,
                LocalDateTime.now(), LocalDateTime.now(), null, null);
        salary = new Salary(-1L, FALSE, senior, 10000, LocalDate.now(), LocalDateTime.now(), null);
        payment = new Payment(-1L, mentoringUser, senior, 20000, "a", "a", "a", LocalDateTime.now(), null, Status.DONE);
        mentoring = new Mentoring(-1L, mentoringUser, senior, payment, salary, "asd", "asd", "1201,1202,1203", 30, WAITING, LocalDateTime.now(), LocalDateTime.now(), null);
    }

    @Test
    @DisplayName("Detail 반환 테스트")
    void getSeniorMentoringDetail() {
        given(seniorGetService.byUser(user))
                .willReturn(senior);
        given(mentoringGetService.byIdAndSeniorForDetails(mentoringId, senior))
                .willReturn(mentoring);
        given(mentoringMapper.mapToSeniorMentoringDetail(mentoring))
                .willReturn(mock(SeniorMentoringDetailResponse.class));

        assertThat(mentoringSeniorInfoUseCase.getSeniorMentoringDetail(user, mentoringId))
                .isNotNull();
    }

    @Test
    @DisplayName("Detail 반환 실패 테스트")
    void getSeniorMentoringDetailFail() {
        given(seniorGetService.byUser(user))
                .willReturn(senior);
        given(mentoringGetService.byIdAndSeniorForDetails(mentoringId, senior))
                .willThrow(MentoringNotFoundException.class);

        assertThatThrownBy(() -> mentoringSeniorInfoUseCase.getSeniorMentoringDetail(user, mentoringId))
                .isInstanceOf(MentoringNotFoundException.class);
    }

    @Test
    @DisplayName("WAITING 반환 테스트")
    void getSeniorWaiting() {
        User user = mock(User.class);
        Senior senior = mock(Senior.class);
        Payment payment = mock(Payment.class);

        Mentoring mentoring1 = new Mentoring(1L, user, senior, payment, null, "A", "b", "a", 40, WAITING, LocalDateTime.now(), LocalDateTime.now(), null);
        Mentoring mentoring2 = new Mentoring(2L, user, senior, payment, null, "A", "b", "a", 40, WAITING, LocalDateTime.now(), LocalDateTime.now(), null);
        Mentoring mentoring3 = new Mentoring(3L, user, senior, payment, null, "A", "b", "a", 40, WAITING, LocalDateTime.now(), LocalDateTime.now(), null);
        List<Mentoring> mentorings = List.of(mentoring1, mentoring2, mentoring3);

        given(seniorGetService.byUser(user))
                .willReturn(senior);
        given(mentoringGetService.bySeniorWaiting(senior))
                .willReturn(mentorings);

        WaitingSeniorMentoringResponse seniorWaiting = mentoringSeniorInfoUseCase.getSeniorWaiting(user);

        assertThat(seniorWaiting.seniorMentoringInfos())
                .hasSameSizeAs(mentorings);
    }

    @Test
    @DisplayName("EXPECTED 반환 테스트")
    void getSeniorExpected() {
        User user = mock(User.class);
        Senior senior = mock(Senior.class);
        Payment payment = mock(Payment.class);

        Mentoring mentoring1 = new Mentoring(1L, user, senior, payment, null, "A", "b", "2024-01-20-17-00", 40, WAITING, LocalDateTime.now(), LocalDateTime.now(), null);
        Mentoring mentoring2 = new Mentoring(2L, user, senior, payment, null, "A", "b", "2024-01-20-17-00", 40, WAITING, LocalDateTime.now(), LocalDateTime.now(), null);
        Mentoring mentoring3 = new Mentoring(3L, user, senior, payment, null, "A", "b", "2024-01-20-17-00", 40, WAITING, LocalDateTime.now(), LocalDateTime.now(), null);
        List<Mentoring> mentorings = List.of(mentoring1, mentoring2, mentoring3);

        given(seniorGetService.byUser(user))
                .willReturn(senior);
        given(mentoringGetService.bySeniorExpected(senior))
                .willReturn(mentorings);

        ExpectedSeniorMentoringResponse seniorExpected = mentoringSeniorInfoUseCase.getSeniorExpected(user);

        assertThat(seniorExpected.seniorMentoringInfos())
                .hasSameSizeAs(mentorings);
    }

    @Test
    @DisplayName("DONE 반환 테스트")
    void getSeniorDone() {
        User user = mock(User.class);
        Senior senior = mock(Senior.class);
        Salary salary = mock(Salary.class);

        Payment payment1 = new Payment(1l, user, senior, 10000, "1", "1", "a", LocalDateTime.now(), LocalDateTime.now(), Status.DONE);
        Payment payment2 = new Payment(2l, user, senior, 10000, "1", "1", "a", LocalDateTime.now(), LocalDateTime.now(), Status.DONE);
        Payment payment3 = new Payment(3l, user, senior, 10000, "1", "1", "a", LocalDateTime.now(), LocalDateTime.now(), Status.DONE);

        Mentoring mentoring1 = new Mentoring(1L, user, senior, payment1, salary, "A", "b", "2024-03-02-18-18", 40, WAITING, LocalDateTime.now(), LocalDateTime.now(), null);
        Mentoring mentoring2 = new Mentoring(2L, user, senior, payment2, salary, "A", "b", "2024-02-02-18-18", 40, WAITING, LocalDateTime.now(), LocalDateTime.now(), null);
        Mentoring mentoring3 = new Mentoring(3L, user, senior, payment3, salary, "A", "b", "2024-01-02-18-18", 40, WAITING, LocalDateTime.now(), LocalDateTime.now(), null);
        List<Mentoring> mentorings = List.of(mentoring1, mentoring2, mentoring3);

        given(seniorGetService.byUser(user))
                .willReturn(senior);
        given(mentoringGetService.bySeniorDone(senior))
                .willReturn(mentorings);
        given(mentoringMapper.mapToSeniorDoneInfo(mentoring1))
                .willReturn(new DoneSeniorMentoringInfo(mentoring1.getMentoringId(), "a", "a", 30, mentoring1.getDate(), LocalDate.now(), true));
        given(mentoringMapper.mapToSeniorDoneInfo(mentoring2))
                .willReturn(new DoneSeniorMentoringInfo(mentoring2.getMentoringId(), "a", "a", 30, mentoring2.getDate(), LocalDate.now(), true));
        given(mentoringMapper.mapToSeniorDoneInfo(mentoring3))
                .willReturn(new DoneSeniorMentoringInfo(mentoring3.getMentoringId(), "a", "a", 30, mentoring3.getDate(), LocalDate.now(), true));

        DoneSeniorMentoringResponse seniorDone = mentoringSeniorInfoUseCase.getSeniorDone(user);
        List<DoneSeniorMentoringInfo> doneSeniorMentoringInfos = seniorDone.seniorMentoringInfos();
        assertThat(doneSeniorMentoringInfos)
                .hasSameSizeAs(mentorings);
    }
}

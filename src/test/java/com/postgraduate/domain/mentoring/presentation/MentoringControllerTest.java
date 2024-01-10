package com.postgraduate.domain.mentoring.presentation;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.postgraduate.IntegrationTest;
import com.postgraduate.domain.mentoring.application.dto.req.MentoringApplyRequest;
import com.postgraduate.domain.mentoring.domain.entity.Mentoring;
import com.postgraduate.domain.mentoring.domain.entity.constant.Status;
import com.postgraduate.domain.mentoring.domain.repository.MentoringRepository;
import com.postgraduate.domain.salary.domain.entity.Salary;
import com.postgraduate.domain.salary.domain.repository.SalaryRepository;
import com.postgraduate.domain.senior.domain.entity.Info;
import com.postgraduate.domain.senior.domain.entity.Profile;
import com.postgraduate.domain.senior.domain.entity.Senior;
import com.postgraduate.domain.senior.domain.repository.SeniorRepository;
import com.postgraduate.domain.user.domain.entity.User;
import com.postgraduate.domain.user.domain.entity.constant.Role;
import com.postgraduate.domain.user.domain.repository.UserRepository;
import com.postgraduate.global.config.security.jwt.util.JwtUtils;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;

import java.time.LocalDate;
import java.util.stream.Stream;

import static com.postgraduate.domain.senior.domain.entity.constant.Status.WAITING;
import static java.time.LocalDateTime.now;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class MentoringControllerTest extends IntegrationTest {
    private static final String AUTHORIZATION = "Authorization";
    private static final String BEARER = "Bearer ";
    @Autowired
    private ObjectMapper objectMapper;
    @Autowired
    private JwtUtils jwtUtil;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private SeniorRepository seniorRepository;
    @Autowired
    private MentoringRepository mentoringRepository;
    @Autowired
    private SalaryRepository salaryRepository;
    private User user;
    private Senior senior;
    private String accessToken;

    private static Stream<Status> statusProvider() {
        return Stream.of(Status.DONE, Status.CANCEL, Status.REFUSE);
    }

    private static Stream<Status> waitingAndDoneProvider() {
        return Stream.of(Status.DONE, Status.WAITING);
    }

    @BeforeEach
    void setUp() {
        user = new User(0L, 1L, "mail", "후배", "011", "profile", 0, Role.USER, true, now(), now(), false);
        userRepository.save(user);

        User userOfSenior = new User(0L, 2L, "mail", "선배", "012", "profile", 0, Role.SENIOR, true, now(), now(), false);
        userRepository.save(userOfSenior);

        Info info = new Info("major", "서울대학교", "교수님", "키워드1,키워드2", "랩실", "인공지능", false, false, "인공지능,키워드1,키워드2");
        Profile profile = new Profile("저는요", "한줄소개", "대상", "chatLink", 40);
        senior = new Senior(0L, userOfSenior, "certification", WAITING, 0, info, profile, now(), now());
        seniorRepository.save(senior);

        accessToken = jwtUtil.generateAccessToken(user.getUserId(), Role.USER);
    }

    @ParameterizedTest
    @MethodSource("waitingAndDoneProvider")
    @DisplayName("대학생이 확정대기 및 완료 상태의 멘토링 목록을 조회한다")
    void getWaitingMentorings(Status status) throws Exception {
        Mentoring mentoring = new Mentoring(0L, user, senior, "topic", "question", "date", 40, status, now(), now());
        mentoringRepository.save(mentoring);

        mvc.perform(get("/mentoring/me/{status}", status.name().toLowerCase())
                        .header(AUTHORIZATION, BEARER + accessToken))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value("MT200"))
                .andExpect(jsonPath("$.message").value("멘토링 리스트 조회에 성공하였습니다."))
                .andExpect(jsonPath("$.data.mentoringInfos[0].profile").value("profile"))
                .andExpect(jsonPath("$.data.mentoringInfos[0].nickName").value("선배"))
                .andExpect(jsonPath("$.data.mentoringInfos[0].postgradu").value("서울대학교"))
                .andExpect(jsonPath("$.data.mentoringInfos[0].major").value("major"))
                .andExpect(jsonPath("$.data.mentoringInfos[0].lab").value("랩실"))
                .andExpect(jsonPath("$.data.mentoringInfos[0].term").value(40))
                .andExpect(jsonPath("$.data.mentoringInfos[0].chatLink").doesNotExist());
    }

    @Test
    @DisplayName("대학생이 예정된 멘토링 목록을 조회한다")
    void getExpectedMentorings() throws Exception {
        Mentoring expectedMentoring = new Mentoring(0L, user, senior, "topic", "question", "date", 40, Status.EXPECTED, now(), now());
        mentoringRepository.save(expectedMentoring);

        mvc.perform(get("/mentoring/me/expected")
                        .header(AUTHORIZATION, BEARER + accessToken))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value("MT200"))
                .andExpect(jsonPath("$.message").value("멘토링 리스트 조회에 성공하였습니다."))
                .andExpect(jsonPath("$.data.mentoringInfos[0].chatLink").value("chatLink"))
                .andExpect(jsonPath("$.data.mentoringInfos[0].date").value("date"));
    }

    @Test
    @DisplayName("대학생이 멘토링을 상세조회한다.")
    void getMentoringDetail() throws Exception {
        Mentoring mentoring = new Mentoring(0L, user, senior, "topic", "question", "date", 40, Status.WAITING, now(), now());
        mentoringRepository.save(mentoring);

        mvc.perform(get("/mentoring/me/{mentoringId}", mentoring.getMentoringId())
                        .header(AUTHORIZATION, BEARER + accessToken))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value("MT200"))
                .andExpect(jsonPath("$.message").value("멘토링 상세 조회에 성공하였습니다."))
                .andExpect(jsonPath("$.data.mentoringInfos[0].chatLink").doesNotExist());
    }


    @ParameterizedTest
    @MethodSource("statusProvider")
    @DisplayName("완료, 취소, 거절 상태의 멘토링은 상세조회되지 않는다.")
    void getDoneMentoringDetail(Status status) throws Exception {
        Mentoring mentoring = new Mentoring(0L, user, senior, "topic", "question", "date", 40, status, now(), now());
        mentoringRepository.save(mentoring);

        mvc.perform(get("/mentoring/me/{mentoringId}", mentoring.getMentoringId())
                        .header(AUTHORIZATION, BEARER + accessToken))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value("EX701"))
                .andExpect(jsonPath("$.message").value("볼 수 없는 신청서 입니다."));
    }

    @Test
    @DisplayName("대학생이 멘토링을 신청한다.")
    void applyMentoring() throws Exception {
        String request = objectMapper.writeValueAsString(new MentoringApplyRequest(senior.getSeniorId(), "topic", "question", "date1,date2,date3"));

        mvc.perform(post("/mentoring/applying")
                        .header(AUTHORIZATION, BEARER + accessToken)
                        .content(request)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value("MT202"))
                .andExpect(jsonPath("$.message").value("멘토링 신청에 성공하였습니다."));
    }

    @Test
    @DisplayName("대학생이 멘토링을 완료한다.")
    void updateMentoringDone() throws Exception {
        Mentoring mentoring = new Mentoring(0L, user, senior, "topic", "question", "date", 40, Status.EXPECTED, now(), now());
        mentoringRepository.save(mentoring);

        Salary salary = new Salary(0L, false, senior, null, 10000, LocalDate.now(), now(), null, null, null);
        salaryRepository.save(salary);

        mvc.perform(patch("/mentoring/me/{mentoringId}/done", mentoring.getMentoringId())
                        .header(AUTHORIZATION, BEARER + accessToken))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value("MT201"))
                .andExpect(jsonPath("$.message").value("멘토링 상태 갱신에 성공하였습니다."));
    }

    @Test
    @DisplayName("대학생이 멘토링을 취소한다.")
    void updateMentoringCancel() throws Exception {
        Mentoring mentoring = new Mentoring(0L, user, senior, "topic", "question", "date", 40, Status.WAITING, now(), now());
        mentoringRepository.save(mentoring);

        mvc.perform(patch("/mentoring/me/{mentoringId}/cancel", mentoring.getMentoringId())
                        .header(AUTHORIZATION, BEARER + accessToken))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value("MT201"))
                .andExpect(jsonPath("$.message").value("멘토링 상태 갱신에 성공하였습니다."));
    }

    @ParameterizedTest
    @EnumSource(value = Status.class, names = {"WAITING", "EXPECTED", "DONE"})
    @DisplayName("대학원생이 멘토링 목록을 조회한다.")
    void getSeniorMentorings(Status status) throws Exception {
        Mentoring mentoring = new Mentoring(0L, user, senior, "topic", "question", "date", 40, status, now(), now());
        mentoringRepository.save(mentoring);

        Salary salary = new Salary(0L, false, senior, null, 10000, LocalDate.now(), now(), null, null, null);
        salaryRepository.save(salary);

        Payment payment = new Payment(0L, mentoring, salary, 10000, "cardAuthNumber", "cardReceipt", now(), null, DONE);
        paymentRepository.save(payment);

        mvc.perform(get("/mentoring/senior/me/{status}", status.name().toLowerCase())
                        .header(AUTHORIZATION, BEARER + seniorAccessToken))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value("MT200"))
                .andExpect(jsonPath("$.message").value("멘토링 리스트 조회에 성공하였습니다."))
                .andExpect(jsonPath("$.data.seniorMentoringInfos[0].mentoringId").value(mentoring.getMentoringId()));
    }

    @ParameterizedTest
    @EnumSource(value = Status.class, names = {"WAITING", "EXPECTED"})
    @DisplayName("대학원생이 멘토링을 상세조회합니다.")
    void getSeniorMentoringDetails(Status status) throws Exception {
        Mentoring mentoring = new Mentoring(0L, user, senior, "topic", "question", "date", 40, status, now(), now());
        mentoringRepository.save(mentoring);

        mvc.perform(get("/mentoring/senior/me/{mentoringId}", mentoring.getMentoringId())
                        .header(AUTHORIZATION, BEARER + seniorAccessToken))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value("MT200"))
                .andExpect(jsonPath("$.message").value("멘토링 상세 조회에 성공하였습니다."))
                .andExpect(jsonPath("$.data.nickName").value("후배"));

    }

    @ParameterizedTest
    @EnumSource(value = Status.class, names = {"DONE", "CANCEL", "REFUSE"})
    @DisplayName("대학원생의 완료, 취소, 거절 상태의 멘토링은 상세조회되지 않는다.")
    void doNotGetMentoringDetails(Status status) throws Exception {
        Mentoring mentoring = new Mentoring(0L, user, senior, "topic", "question", "date", 40, status, now(), now());
        mentoringRepository.save(mentoring);

        mvc.perform(get("/mentoring/senior/me/{mentoringId}", mentoring.getMentoringId())
                        .header(AUTHORIZATION, BEARER + seniorAccessToken))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value("EX701"))
                .andExpect(jsonPath("$.message").value("볼 수 없는 신청서 입니다."));

    }
}
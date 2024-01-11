package com.postgraduate.domain.mentoring.presentation;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.postgraduate.IntegrationTest;
import com.postgraduate.domain.mentoring.application.dto.req.MentoringApplyRequest;
import com.postgraduate.domain.mentoring.application.dto.req.MentoringDateRequest;
import com.postgraduate.domain.mentoring.domain.entity.Mentoring;
import com.postgraduate.domain.mentoring.domain.entity.constant.Status;
import com.postgraduate.domain.mentoring.domain.repository.MentoringRepository;
import com.postgraduate.domain.payment.domain.entity.Payment;
import com.postgraduate.domain.payment.domain.repository.PaymentRepository;
import com.postgraduate.domain.refuse.application.dto.req.MentoringRefuseRequest;
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
import org.junit.jupiter.params.provider.EnumSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;

import java.time.LocalDate;

import static com.postgraduate.domain.mentoring.presentation.constant.MentoringResponseCode.*;
import static com.postgraduate.domain.mentoring.presentation.constant.MentoringResponseMessage.*;
import static com.postgraduate.domain.payment.domain.entity.constant.Status.DONE;
import static com.postgraduate.domain.salary.util.SalaryUtil.getSalaryDate;
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
    @Autowired
    private PaymentRepository paymentRepository;
    private User user;
    private Senior senior;
    private String userAccessToken;
    private String seniorAccessToken;

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

        userAccessToken = jwtUtil.generateAccessToken(user.getUserId(), Role.USER);
        seniorAccessToken = jwtUtil.generateAccessToken(userOfSenior.getUserId(), Role.SENIOR);
    }

    @ParameterizedTest
    @EnumSource(value = Status.class, names = {"WAITING", "DONE"})
    @DisplayName("대학생이 확정대기 및 완료 상태의 멘토링 목록을 조회한다")
    void getWaitingMentorings(Status status) throws Exception {
        Mentoring mentoring = new Mentoring(0L, user, senior, "topic", "question", "date", 40, status, now(), now());
        mentoringRepository.save(mentoring);

        mvc.perform(get("/mentoring/me/{status}", status.name().toLowerCase())
                        .header(AUTHORIZATION, BEARER + userAccessToken))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(MENTORING_FIND.getCode()))
                .andExpect(jsonPath("$.message").value(GET_MENTORING_LIST_INFO.getMessage()))
                .andExpect(jsonPath("$.data.mentoringInfos[0].mentoringId").value(mentoring.getMentoringId()))
                .andExpect(jsonPath("$.data.mentoringInfos[0].chatLink").doesNotExist());
    }

    @Test
    @DisplayName("대학생이 예정된 멘토링 목록을 조회한다")
    void getExpectedMentorings() throws Exception {
        Mentoring mentoring = new Mentoring(0L, user, senior, "topic", "question", "date", 40, Status.EXPECTED, now(), now());
        mentoringRepository.save(mentoring);

        mvc.perform(get("/mentoring/me/expected")
                        .header(AUTHORIZATION, BEARER + userAccessToken))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(MENTORING_FIND.getCode()))
                .andExpect(jsonPath("$.message").value(GET_MENTORING_LIST_INFO.getMessage()))
                .andExpect(jsonPath("$.data.mentoringInfos[0].mentoringId").value(mentoring.getMentoringId()));
    }

    @ParameterizedTest
    @EnumSource(value = Status.class, names = {"WAITING", "EXPECTED"})
    @DisplayName("대학생이 멘토링을 상세조회한다.")
    void getMentoringDetail(Status status) throws Exception {
        Mentoring mentoring = new Mentoring(0L, user, senior, "topic", "question", "date", 40, status, now(), now());
        mentoringRepository.save(mentoring);

        mvc.perform(get("/mentoring/me/{mentoringId}", mentoring.getMentoringId())
                        .header(AUTHORIZATION, BEARER + userAccessToken))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(MENTORING_FIND.getCode()))
                .andExpect(jsonPath("$.message").value(GET_MENTORING_DETAIL_INFO.getMessage()))
                .andExpect(jsonPath("$.data.seniorId").value(senior.getSeniorId()));
    }


    @ParameterizedTest
    @EnumSource(value = Status.class, names = {"DONE", "CANCEL", "REFUSE"})
    @DisplayName("대학생의 완료, 취소, 거절 상태의 멘토링은 상세조회되지 않는다.")
    void getDoneMentoringDetail(Status status) throws Exception {
        Mentoring mentoring = new Mentoring(0L, user, senior, "topic", "question", "date", 40, status, now(), now());
        mentoringRepository.save(mentoring);

        mvc.perform(get("/mentoring/me/{mentoringId}", mentoring.getMentoringId())
                        .header(AUTHORIZATION, BEARER + userAccessToken))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(DETAIL_NOT_FOUND.getCode()))
                .andExpect(jsonPath("$.message").value(NOT_FOUND_DETAIL.getMessage()));
    }

    @Test
    @DisplayName("대학생이 멘토링을 신청한다.")
    void applyMentoring() throws Exception {
        String request = objectMapper.writeValueAsString(new MentoringApplyRequest(senior.getSeniorId(), "topic", "question", "date1,date2,date3"));

        mvc.perform(post("/mentoring/applying")
                        .header(AUTHORIZATION, BEARER + userAccessToken)
                        .content(request)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(MENTORING_CREATE.getCode()))
                .andExpect(jsonPath("$.message").value(CREATE_MENTORING.getMessage()));
    }

    @Test
    @DisplayName("대학생이 멘토링을 완료한다.")
    void updateMentoringDone() throws Exception {
        Mentoring mentoring = new Mentoring(0L, user, senior, "topic", "question", "date", 40, Status.EXPECTED, now(), now());
        mentoringRepository.save(mentoring);

        Salary salary = new Salary(0L, false, senior, null, 10000, getSalaryDate(), now(), null, null, null);
        salaryRepository.save(salary);

        mvc.perform(patch("/mentoring/me/{mentoringId}/done", mentoring.getMentoringId())
                        .header(AUTHORIZATION, BEARER + userAccessToken))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(MENTORING_UPDATE.getCode()))
                .andExpect(jsonPath("$.message").value(UPDATE_MENTORING.getMessage()));
    }

    @ParameterizedTest
    @EnumSource(value = Status.class, names = {"WAITING", "DONE", "CANCEL", "REFUSE"})
    @DisplayName("진행예정이 아닌 멘토링의 경우 완료할 수 없다.")
    void updateMentoringDoneWithoutExpected(Status status) throws Exception {
        Mentoring mentoring = new Mentoring(0L, user, senior, "topic", "question", "date", 40, status, now(), now());
        mentoringRepository.save(mentoring);

        Salary salary = new Salary(0L, false, senior, null, 10000, getSalaryDate(), now(), null, null, null);
        salaryRepository.save(salary);

        mvc.perform(patch("/mentoring/me/{mentoringId}/done", mentoring.getMentoringId())
                        .header(AUTHORIZATION, BEARER + userAccessToken))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(MENTORING_NOT_EXPECTED.getCode()))
                .andExpect(jsonPath("$.message").value(NOT_EXPECTED_MENTORING.getMessage()));
    }

    @Test
    @DisplayName("대학생이 멘토링을 취소한다.")
    void updateMentoringCancel() throws Exception {
        Mentoring mentoring = new Mentoring(0L, user, senior, "topic", "question", "date", 40, Status.WAITING, now(), now());
        mentoringRepository.save(mentoring);

        mvc.perform(patch("/mentoring/me/{mentoringId}/cancel", mentoring.getMentoringId())
                        .header(AUTHORIZATION, BEARER + userAccessToken))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(MENTORING_UPDATE.getCode()))
                .andExpect(jsonPath("$.message").value(UPDATE_MENTORING.getMessage()));
    }

    @ParameterizedTest
    @EnumSource(value = Status.class, names = {"EXPECTED", "DONE", "CANCEL", "REFUSE"})
    @DisplayName("멘토링이 확정대기 상태가 아니라면 취소할 수 없다.")
    void updateMentoringCancelWithoutWaiting(Status status) throws Exception {
        Mentoring mentoring = new Mentoring(0L, user, senior, "topic", "question", "date", 40, status, now(), now());
        mentoringRepository.save(mentoring);

        mvc.perform(patch("/mentoring/me/{mentoringId}/cancel", mentoring.getMentoringId())
                        .header(AUTHORIZATION, BEARER + userAccessToken))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(MENTORING_NOT_WAITING.getCode()))
                .andExpect(jsonPath("$.message").value(NOT_WAITING_MENTORING.getMessage()));
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
                .andExpect(jsonPath("$.code").value(MENTORING_FIND.getCode()))
                .andExpect(jsonPath("$.message").value(GET_MENTORING_LIST_INFO.getMessage()))
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
                .andExpect(jsonPath("$.code").value(MENTORING_FIND.getCode()))
                .andExpect(jsonPath("$.message").value(GET_MENTORING_DETAIL_INFO.getMessage()))
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
                .andExpect(jsonPath("$.code").value(DETAIL_NOT_FOUND.getCode()))
                .andExpect(jsonPath("$.message").value(NOT_FOUND_DETAIL.getMessage()));
    }

    @Test
    @DisplayName("대학원생이 멘토링을 수락한다.")
    void updateSeniorMentoringExpected() throws Exception {
        Mentoring mentoring = new Mentoring(0L, user, senior, "topic", "question", "date1,date2,date3", 40, Status.WAITING, now(), now());
        mentoringRepository.save(mentoring);

        String request = objectMapper.writeValueAsString(new MentoringDateRequest("date1"));
        mvc.perform(patch("/mentoring/senior/me/{mentoringId}/expected", mentoring.getMentoringId())
                        .header(AUTHORIZATION, BEARER + seniorAccessToken)
                        .content(request)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(MENTORING_UPDATE.getCode()))
                .andExpect(jsonPath("$.message").value(UPDATE_MENTORING.getMessage()));
    }

    @ParameterizedTest
    @EnumSource(value = Status.class, names = {"EXPECTED", "DONE", "CANCEL", "REFUSE"})
    @DisplayName("멘토링이 확정대기 상태가 아니라면 수락할 수 없다.")
    void updateSeniorMentoringExpectedWithoutWaiting(Status status) throws Exception {
        Mentoring mentoring = new Mentoring(0L, user, senior, "topic", "question", "date1,date2,date3", 40, status, now(), now());
        mentoringRepository.save(mentoring);

        String request = objectMapper.writeValueAsString(new MentoringDateRequest("date1"));
        mvc.perform(patch("/mentoring/senior/me/{mentoringId}/expected", mentoring.getMentoringId())
                        .header(AUTHORIZATION, BEARER + seniorAccessToken)
                        .content(request)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(MENTORING_NOT_WAITING.getCode()))
                .andExpect(jsonPath("$.message").value(NOT_WAITING_MENTORING.getMessage()));
    }

    @Test
    @DisplayName("대학원생이 멘토링을 거절한다.")
    void updateSeniorMentoringRefuse() throws Exception {
        Mentoring mentoring = new Mentoring(0L, user, senior, "topic", "question", "date1,date2,date3", 40, Status.WAITING, now(), now());
        mentoringRepository.save(mentoring);

        String request = objectMapper.writeValueAsString(new MentoringRefuseRequest("reason"));
        mvc.perform(patch("/mentoring/senior/me/{mentoringId}/refuse", mentoring.getMentoringId())
                        .header(AUTHORIZATION, BEARER + seniorAccessToken)
                        .content(request)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(MENTORING_UPDATE.getCode()))
                .andExpect(jsonPath("$.message").value(UPDATE_MENTORING.getMessage()));
    }

    @ParameterizedTest
    @EnumSource(value = Status.class, names = {"EXPECTED", "DONE", "CANCEL", "REFUSE"})
    @DisplayName("멘토링이 확정대기 상태가 아니라면 거절할 수 없다.")
    void updateSeniorMentoringRefuseWithoutWaiting(Status status) throws Exception {
        Mentoring mentoring = new Mentoring(0L, user, senior, "topic", "question", "date", 40, status, now(), now());
        mentoringRepository.save(mentoring);

        String request = objectMapper.writeValueAsString(new MentoringRefuseRequest("reason"));
        mvc.perform(patch("/mentoring/senior/me/{mentoringId}/refuse", mentoring.getMentoringId())
                        .header(AUTHORIZATION, BEARER + seniorAccessToken)
                        .content(request)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(MENTORING_NOT_WAITING.getCode()))
                .andExpect(jsonPath("$.message").value(NOT_WAITING_MENTORING.getMessage()));
    }
}
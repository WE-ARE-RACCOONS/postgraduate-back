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
import com.postgraduate.global.exception.constant.ErrorCode;
import com.postgraduate.global.slack.SlackLogErrorMessage;
import com.postgraduate.global.slack.SlackSalaryMessage;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EnumSource;
import org.junit.jupiter.params.provider.NullAndEmptySource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;

import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static com.postgraduate.domain.auth.presentation.constant.AuthResponseCode.AUTH_DENIED;
import static com.postgraduate.domain.auth.presentation.constant.AuthResponseMessage.PERMISSION_DENIED;
import static com.postgraduate.domain.mentoring.presentation.constant.MentoringResponseCode.*;
import static com.postgraduate.domain.mentoring.presentation.constant.MentoringResponseMessage.*;
import static com.postgraduate.domain.payment.domain.entity.constant.Status.DONE;
import static com.postgraduate.domain.salary.util.SalaryUtil.getSalaryDate;
import static com.postgraduate.domain.senior.domain.entity.constant.Status.WAITING;
import static java.time.LocalDateTime.now;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
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
    @MockBean
    private SlackLogErrorMessage slackLogErrorMessage;
    private User user;
    private Senior senior;
    private Payment payment;
    private Salary salary;
    private String userAccessToken;
    private String seniorAccessToken;

    @BeforeEach
    void setUp() throws IOException {
        user = new User(0L, 1L, "mail", "후배", "011", "profile", 0, Role.USER, true, now(), now(), false);
        userRepository.save(user);

        User userOfSenior = new User(0L, 2L, "mail", "선배", "012", "profile", 0, Role.SENIOR, true, now(), now(), false);
        userRepository.save(userOfSenior);

        Info info = new Info("major", "서울대학교", "교수님", "키워드1,키워드2", "랩실", "인공지능", false, false, "인공지능,키워드1,키워드2");
        Profile profile = new Profile("저는요", "한줄소개", "대상", "chatLink", 40);
        senior = new Senior(0L, userOfSenior, "certification", WAITING, 0, info, profile, now(), now());
        seniorRepository.save(senior);

        List<Payment> payments = new ArrayList<>();
        salary = new Salary(0L, false, senior, payments, 20000, LocalDate.now(), LocalDateTime.now(), null, null, null);
        salaryRepository.save(salary);

        payment = new Payment(0L, salary, user, 20000, "1", "123", "123", LocalDateTime.now(), LocalDateTime.now(), DONE);
        paymentRepository.save(payment);

        userAccessToken = jwtUtil.generateAccessToken(user.getUserId(), Role.USER);
        seniorAccessToken = jwtUtil.generateAccessToken(userOfSenior.getUserId(), Role.SENIOR);

        doNothing().when(slackLogErrorMessage).sendSlackLog(any());
    }

    @ParameterizedTest
    @EnumSource(value = Status.class, names = {"WAITING", "DONE"})
    @DisplayName("대학생이 확정대기 및 완료 상태의 멘토링 목록을 조회한다")
    void getWaitingMentorings(Status status) throws Exception {
        Mentoring mentoring = new Mentoring(0L, user, senior, payment, "topic", "question", "date", 40, status, now(), now());
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
        Mentoring mentoring = new Mentoring(0L, user, senior, payment, "topic", "question", "date", 40, Status.EXPECTED, now(), now());
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
        Mentoring mentoring = new Mentoring(0L, user, senior, payment, "topic", "question", "date", 40, status, now(), now());
        mentoringRepository.save(mentoring);

        mvc.perform(get("/mentoring/me/{mentoringId}", mentoring.getMentoringId())
                        .header(AUTHORIZATION, BEARER + userAccessToken))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(MENTORING_FIND.getCode()))
                .andExpect(jsonPath("$.message").value(GET_MENTORING_DETAIL_INFO.getMessage()))
                .andExpect(jsonPath("$.data.seniorId").value(senior.getSeniorId()));
    }

    @Test
    @DisplayName("자신이 신청한 멘토링이 아니라면 상세조회되지 않는다")
    void getOtherMentoringDetail() throws Exception {
        User otherUser = new User(-1L, 0L, "mail", "다른 후배", "011", "profile", 0, Role.USER, true, now(), now(), false);
        userRepository.save(otherUser);
        Mentoring mentoring = new Mentoring(0L, otherUser, senior, payment, "topic", "question", "date", 40, Status.EXPECTED, now(), now());
        mentoringRepository.save(mentoring);

        mvc.perform(get("/mentoring/me/{mentoringId}", mentoring.getMentoringId())
                        .header(AUTHORIZATION, BEARER + userAccessToken))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(AUTH_DENIED.getCode()))
                .andExpect(jsonPath("$.message").value(PERMISSION_DENIED.getMessage()));
    }


    @ParameterizedTest
    @EnumSource(value = Status.class, names = {"DONE", "CANCEL", "REFUSE"})
    @DisplayName("대학생의 완료, 취소, 거절 상태의 멘토링은 상세조회되지 않는다.")
    void getDoneMentoringDetail(Status status) throws Exception {
        Mentoring mentoring = new Mentoring(0L, user, senior, payment, "topic", "question", "date", 40, status, now(), now());
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
        String request = objectMapper.writeValueAsString(new MentoringApplyRequest("1", "topic", "question", "date1,date2,date3"));

        mvc.perform(post("/mentoring/applying")
                        .header(AUTHORIZATION, BEARER + userAccessToken)
                        .content(request)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(MENTORING_CREATE.getCode()))
                .andExpect(jsonPath("$.message").value(CREATE_MENTORING.getMessage()));
    }

//    @ParameterizedTest
//    @ValueSource(strings = {"date1", "date1,date2", "date1,date2,date3,date4"})
//    @DisplayName("날짜가 3개가 아니라면 멘토링을 신청할 수 없다.")
//    void applyMentoringWithoutThreeDates(String date) throws Exception {
//        String request = objectMapper.writeValueAsString(new MentoringApplyRequest("1", "topic", "question", date));
//
//        mvc.perform(post("/mentoring/applying")
//                        .header(AUTHORIZATION, BEARER + userAccessToken)
//                        .content(request)
//                        .contentType(MediaType.APPLICATION_JSON)
//                        .accept(MediaType.APPLICATION_JSON))
//                .andExpect(status().isOk())
//                .andExpect(jsonPath("$.code").value(MentoringResponseCode.INVALID_DATE.getCode()))
//                .andExpect(jsonPath("$.message").value(MentoringResponseMessage.INVALID_DATE.getMessage()));
//
//    }
//todo:    환불 로직 추가되어 수정 필요

//    @ParameterizedTest
//    @NullAndEmptySource
//    @DisplayName("신청서가 빈 칸이라면 멘토링을 신청할 수 없다")
//    void emptyApplyMentoring(String empty) throws Exception {
//        String request = objectMapper.writeValueAsString(new MentoringApplyRequest("1", empty, empty, empty));
//
//        mvc.perform(post("/mentoring/applying")
//                        .header(AUTHORIZATION, BEARER + userAccessToken)
//                        .content(request)
//                        .contentType(MediaType.APPLICATION_JSON)
//                        .accept(MediaType.APPLICATION_JSON))
//                .andExpect(status().isOk())
//                .andExpect(jsonPath("$.code").value(ErrorCode.VALID_BLANK.getCode()));
//    }
//todo: 환불 로직 추가되어 수정 필요

    @Test
    @DisplayName("대학생이 멘토링을 완료한다.")
    void updateMentoringDone() throws Exception {
        Mentoring mentoring = new Mentoring(0L, user, senior, payment, "topic", "question", "date", 40, Status.EXPECTED, now(), now());
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
        Mentoring mentoring = new Mentoring(0L, user, senior, payment, "topic", "question", "date", 40, status, now(), now());
        mentoringRepository.save(mentoring);

        Salary salary = new Salary(0L, false, senior, null, 10000, getSalaryDate(), now(), null, null, null);
        salaryRepository.save(salary);

        mvc.perform(patch("/mentoring/me/{mentoringId}/done", mentoring.getMentoringId())
                        .header(AUTHORIZATION, BEARER + userAccessToken))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(MENTORING_NOT_EXPECTED.getCode()))
                .andExpect(jsonPath("$.message").value(NOT_EXPECTED_MENTORING.getMessage()));
    }

//    @Test
//    @DisplayName("대학생이 멘토링을 취소한다.")
//    void updateMentoringCancel() throws Exception {
//        Mentoring mentoring = new Mentoring(0L, user, senior, payment, "topic", "question", "date", 40, Status.WAITING, now(), now());
//        mentoringRepository.save(mentoring);
//
//        mvc.perform(patch("/mentoring/me/{mentoringId}/cancel", mentoring.getMentoringId())
//                        .header(AUTHORIZATION, BEARER + userAccessToken))
//                .andExpect(status().isOk())
//                .andExpect(jsonPath("$.code").value(MENTORING_UPDATE.getCode()))
//                .andExpect(jsonPath("$.message").value(UPDATE_MENTORING.getMessage()));
//    }
    //todo : 환불 관련하여 작성 필요 (환불 처리에 대한 코드가 발생하여 다를 수 있음)

    @ParameterizedTest
    @EnumSource(value = Status.class, names = {"EXPECTED", "DONE", "CANCEL", "REFUSE"})
    @DisplayName("멘토링이 확정대기 상태가 아니라면 취소할 수 없다.")
    void updateMentoringCancelWithoutWaiting(Status status) throws Exception {
        Mentoring mentoring = new Mentoring(0L, user, senior, payment, "topic", "question", "date", 40, status, now(), now());
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
        Mentoring mentoring = new Mentoring(0L, user, senior, payment, "topic", "question", "2024-01-20-18-00", 40, status, now(), now());
        mentoringRepository.save(mentoring);

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
        Mentoring mentoring = new Mentoring(0L, user, senior, payment, "topic", "question", "date", 40, status, now(), now());
        mentoringRepository.save(mentoring);

        mvc.perform(get("/mentoring/senior/me/{mentoringId}", mentoring.getMentoringId())
                        .header(AUTHORIZATION, BEARER + seniorAccessToken))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(MENTORING_FIND.getCode()))
                .andExpect(jsonPath("$.message").value(GET_MENTORING_DETAIL_INFO.getMessage()))
                .andExpect(jsonPath("$.data.nickName").value("후배"));

    }

    @Test
    @DisplayName("자신이 신청받은 멘토링이 아니라면 상세조회되지 않는다")
    void getOtherSeniorMentoringDetail() throws Exception {
        User otherUser = new User(-1L, 0L, "mail", "다른 후배", "011", "profile", 0, Role.USER, true, now(), now(), false);
        userRepository.save(otherUser);

        Info info = new Info("major", "서울대학교", "교수님", "키워드1,키워드2", "랩실", "인공지능", false, false, "인공지능,키워드1,키워드2");
        Senior otherSenior = new Senior(-1L, otherUser, "certification", WAITING, 0, info, null, now(), now());
        seniorRepository.save(otherSenior);

        Mentoring mentoring = new Mentoring(0L, otherUser, otherSenior, payment, "topic", "question", "date", 40, Status.EXPECTED, now(), now());
        mentoringRepository.save(mentoring);

        mvc.perform(get("/mentoring/me/{mentoringId}", mentoring.getMentoringId())
                        .header(AUTHORIZATION, BEARER + userAccessToken))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(AUTH_DENIED.getCode()))
                .andExpect(jsonPath("$.message").value(PERMISSION_DENIED.getMessage()));
    }

    @ParameterizedTest
    @EnumSource(value = Status.class, names = {"DONE", "CANCEL", "REFUSE"})
    @DisplayName("대학원생의 완료, 취소, 거절 상태의 멘토링은 상세조회되지 않는다.")
    void doNotGetMentoringDetails(Status status) throws Exception {
        Mentoring mentoring = new Mentoring(0L, user, senior, payment, "topic", "question", "date", 40, status, now(), now());
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
        Mentoring mentoring = new Mentoring(0L, user, senior, payment, "topic", "question", "date1,date2,date3", 40, Status.WAITING, now(), now());
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
        Mentoring mentoring = new Mentoring(0L, user, senior, payment, "topic", "question", "date1,date2,date3", 40, status, now(), now());
        mentoringRepository.save(mentoring);

        String request = objectMapper.writeValueAsString(new MentoringDateRequest("date1"));
        mvc.perform(patch("/mentoring/senior/me/{mentoringId}/expected", mentoring.getMentoringId())
                        .header(AUTHORIZATION, BEARER + seniorAccessToken)
                        .content(request)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(MENTORING_NOT_WAITING.getCode()));
    }

    @ParameterizedTest
    @NullAndEmptySource
    @DisplayName("확정날짜가 비어있다면 멘토링을 수락할 수 없다")
    void updateSeniorMentoringExpectedWithoutDate(String empty) throws Exception {
        Mentoring mentoring = new Mentoring(0L, user, senior, payment, "topic", "question", "date1,date2,date3", 40, Status.WAITING, now(), now());
        mentoringRepository.save(mentoring);

        String request = objectMapper.writeValueAsString(new MentoringDateRequest(empty));
        mvc.perform(patch("/mentoring/senior/me/{mentoringId}/expected", mentoring.getMentoringId())
                        .header(AUTHORIZATION, BEARER + seniorAccessToken)
                        .content(request)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(ErrorCode.VALID_BLANK.getCode()));
    }

//    @Test
//    @DisplayName("대학원생이 멘토링을 거절한다.")
//    void updateSeniorMentoringRefuse() throws Exception {
//        Mentoring mentoring = new Mentoring(0L, user, senior, payment, "topic", "question", "date1,date2,date3", 40, Status.WAITING, now(), now());
//        mentoringRepository.save(mentoring);
//
//        String request = objectMapper.writeValueAsString(new MentoringRefuseRequest("reason"));
//        mvc.perform(patch("/mentoring/senior/me/{mentoringId}/refuse", mentoring.getMentoringId())
//                        .header(AUTHORIZATION, BEARER + seniorAccessToken)
//                        .content(request)
//                        .contentType(MediaType.APPLICATION_JSON)
//                        .accept(MediaType.APPLICATION_JSON))
//                .andExpect(status().isOk())
//                .andExpect(jsonPath("$.code").value(MENTORING_UPDATE.getCode()))
//                .andExpect(jsonPath("$.message").value(UPDATE_MENTORING.getMessage()));
//    }
    //todo : 환불 처리 요청(payple) 처리 필요

    @ParameterizedTest
    @EnumSource(value = Status.class, names = {"EXPECTED", "DONE", "CANCEL", "REFUSE"})
    @DisplayName("멘토링이 확정대기 상태가 아니라면 거절할 수 없다.")
    void updateSeniorMentoringRefuseWithoutWaiting(Status status) throws Exception {
        Mentoring mentoring = new Mentoring(0L, user, senior, payment, "topic", "question", "date", 40, status, now(), now());
        mentoringRepository.save(mentoring);

        String request = objectMapper.writeValueAsString(new MentoringRefuseRequest("reason"));
        mvc.perform(patch("/mentoring/senior/me/{mentoringId}/refuse", mentoring.getMentoringId())
                        .header(AUTHORIZATION, BEARER + seniorAccessToken)
                        .content(request)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(MENTORING_NOT_WAITING.getCode()));
    }

    @ParameterizedTest
    @NullAndEmptySource
    @DisplayName("사유가 비어있다면 멘토링을 거절할 수 없다")
    void updateSeniorMentoringExpectedWithoutRefuse(String empty) throws Exception {
        Mentoring mentoring = new Mentoring(0L, user, senior, payment, "topic", "question", "date1,date2,date3", 40, Status.WAITING, now(), now());
        mentoringRepository.save(mentoring);

        String request = objectMapper.writeValueAsString(new MentoringRefuseRequest(empty));
        mvc.perform(patch("/mentoring/senior/me/{mentoringId}/refuse", mentoring.getMentoringId())
                        .header(AUTHORIZATION, BEARER + seniorAccessToken)
                        .content(request)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(ErrorCode.VALID_BLANK.getCode()));
    }
}
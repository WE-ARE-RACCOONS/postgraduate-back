package com.postgraduate.Integration;

import com.postgraduate.domain.mentoring.application.dto.req.MentoringDateRequest;
import com.postgraduate.domain.mentoring.domain.entity.Mentoring;
import com.postgraduate.domain.mentoring.domain.entity.constant.Status;
import com.postgraduate.domain.payment.domain.entity.Payment;
import com.postgraduate.domain.refuse.application.dto.req.MentoringRefuseRequest;
import com.postgraduate.domain.salary.domain.entity.Salary;
import com.postgraduate.domain.senior.domain.entity.Info;
import com.postgraduate.domain.senior.domain.entity.Senior;
import com.postgraduate.domain.user.domain.entity.User;
import com.postgraduate.domain.user.domain.entity.constant.Role;
import com.postgraduate.global.constant.ErrorCode;
import com.postgraduate.support.IntegrationTest;
import com.postgraduate.support.Resource;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EnumSource;
import org.junit.jupiter.params.provider.NullAndEmptySource;
import org.springframework.http.MediaType;

import java.io.IOException;

import static com.postgraduate.domain.auth.presentation.constant.AuthResponseCode.AUTH_DENIED;
import static com.postgraduate.domain.auth.presentation.constant.AuthResponseMessage.PERMISSION_DENIED;
import static com.postgraduate.domain.mentoring.presentation.constant.MentoringResponseCode.*;
import static com.postgraduate.domain.mentoring.presentation.constant.MentoringResponseMessage.*;
import static com.postgraduate.domain.senior.domain.entity.constant.Status.WAITING;
import static java.time.LocalDateTime.now;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class MentoringControllerTest extends IntegrationTest {
    private Resource resource = new Resource();
    private static final String AUTHORIZATION = "Authorization";
    private static final String BEARER = "Bearer ";
    private User user;
    private User seniorUser;
    private User otherUser;
    private Senior senior;
    private Senior otherSenior;
    private Payment payment;
    private Salary salary;
    private String userAccessToken;
    private String seniorAccessToken;

    @BeforeEach
    void setUp() throws IOException {
        user = resource.getUser();
        userRepository.save(user);

        seniorUser = resource.getSeniorUser();
        userRepository.save(seniorUser);

        otherUser = resource.getOtherUser();
        userRepository.save(otherUser);

        senior = resource.getSenior();
        seniorRepository.save(senior);

        otherSenior = resource.getOtherSenior();
        seniorRepository.save(otherSenior);

        salary = resource.getSalary();
        salaryRepository.save(salary);

        payment = resource.getPayment();
        paymentRepository.save(payment);

        userAccessToken = jwtUtil.generateAccessToken(user.getUserId(), Role.USER);
        seniorAccessToken = jwtUtil.generateAccessToken(seniorUser.getUserId(), Role.SENIOR);

        doNothing().when(slackLogErrorMessage).sendSlackLog(any());
    }

    @ParameterizedTest
    @EnumSource(value = Status.class, names = {"WAITING", "DONE"})
    @DisplayName("대학생이 확정대기 및 완료 상태의 멘토링 목록을 조회한다")
    void getWaitingMentorings(Status status) throws Exception {
        Mentoring mentoring = new Mentoring(-1L, user, senior, payment, salary, "topic", "question", "date", 40, status, now(), now());
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
        Mentoring mentoring = new Mentoring(-1L, user, senior, payment, null, "topic", "question", "date", 40, Status.EXPECTED, now(), now());
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
        Mentoring mentoring = new Mentoring(-1L, user, senior, payment, null, "topic", "question", "date", 40, status, now(), now());
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
        Mentoring mentoring = new Mentoring(-1L, otherUser, senior, payment, null, "topic", "question", "date", 40, Status.EXPECTED, now(), now());
        mentoringRepository.save(mentoring);

        mvc.perform(get("/mentoring/me/{mentoringId}", mentoring.getMentoringId())
                        .header(AUTHORIZATION, BEARER + userAccessToken))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(MENTORING_NOT_FOUND.getCode()))
                .andExpect(jsonPath("$.message").value(NOT_FOUND_MENTORING.getMessage()));
    }


    @ParameterizedTest
    @EnumSource(value = Status.class, names = {"DONE", "CANCEL", "REFUSE"})
    @DisplayName("대학생의 완료, 취소, 거절 상태의 멘토링은 상세조회되지 않는다.")
    void getDoneMentoringDetail(Status status) throws Exception {
        Mentoring mentoring = new Mentoring(-1L, user, senior, payment, null, "topic", "question", "date", 40, status, now(), now());
        mentoringRepository.save(mentoring);

        mvc.perform(get("/mentoring/me/{mentoringId}", mentoring.getMentoringId())
                        .header(AUTHORIZATION, BEARER + userAccessToken))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(MENTORING_NOT_FOUND.getCode()))
                .andExpect(jsonPath("$.message").value(NOT_FOUND_MENTORING.getMessage()));
    }

//    @Test
//    @DisplayName("대학생이 멘토링을 신청한다.")
//    void applyMentoring() throws Exception {
//        String request = objectMapper.writeValueAsString(new MentoringApplyRequest("1", "topic", "question", "date1,date2,date3"));
//
//        mvc.perform(post("/mentoring/applying")
//                        .header(AUTHORIZATION, BEARER + userAccessToken)
//                        .content(request)
//                        .contentType(MediaType.APPLICATION_JSON)
//                        .accept(MediaType.APPLICATION_JSON))
//                .andExpect(status().isOk())
//                .andExpect(jsonPath("$.code").value(MENTORING_CREATE.getCode()))
//                .andExpect(jsonPath("$.message").value(CREATE_MENTORING.getMessage()));
//    }

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
        Mentoring mentoring = new Mentoring(-1L, user, senior, payment, null, "topic", "question", "date", 40, Status.EXPECTED, now(), now());
        mentoringRepository.save(mentoring);

        mvc.perform(patch("/mentoring/me/{mentoringId}/done", mentoring.getMentoringId())
                        .header(AUTHORIZATION, BEARER + userAccessToken))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(MENTORING_UPDATE.getCode()))
                .andExpect(jsonPath("$.message").value(UPDATE_MENTORING.getMessage()));
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
    @EnumSource(value = Status.class, names = {"WAITING", "EXPECTED"})
    @DisplayName("대학원생이 멘토링을 상세조회합니다.")
    void getSeniorMentoringDetails(Status status) throws Exception {
        Mentoring mentoring = new Mentoring(-1L, user, senior, payment, null, "topic", "question", "date", 40, status, now(), now());
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
        Mentoring mentoring = new Mentoring(-1L, otherUser, otherSenior, payment, null, "topic", "question", "date", 40, Status.EXPECTED, now(), now());
        mentoringRepository.save(mentoring);

        mvc.perform(get("/mentoring/senior/me/{mentoringId}", mentoring.getMentoringId())
                        .header(AUTHORIZATION, BEARER + userAccessToken))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(AUTH_DENIED.getCode()))
                .andExpect(jsonPath("$.message").value(PERMISSION_DENIED.getMessage()));
    }

    @ParameterizedTest
    @EnumSource(value = Status.class, names = {"DONE", "CANCEL", "REFUSE"})
    @DisplayName("대학원생의 완료, 취소, 거절 상태의 멘토링은 상세조회되지 않는다.")
    void doNotGetMentoringDetails(Status status) throws Exception {
        Mentoring mentoring = new Mentoring(-1L, user, senior, payment, null, "topic", "question", "date", 40, status, now(), now());
        mentoringRepository.save(mentoring);

        mvc.perform(get("/mentoring/senior/me/{mentoringId}", mentoring.getMentoringId())
                        .header(AUTHORIZATION, BEARER + seniorAccessToken))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(MENTORING_NOT_FOUND.getCode()))
                .andExpect(jsonPath("$.message").value(NOT_FOUND_MENTORING.getMessage()));
    }

    @Test
    @DisplayName("대학원생이 멘토링을 수락한다.")
    void updateSeniorMentoringExpected() throws Exception {
        Mentoring mentoring = new Mentoring(-1L, user, senior, payment, null, "topic", "question", "2024-04-18-18-00,2024-04-18-18-00,2024-04-18-18-00", 40, Status.WAITING, now(), now());
        mentoringRepository.save(mentoring);

        String request = objectMapper.writeValueAsString(new MentoringDateRequest("2024-04-18-18-00"));
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
    @NullAndEmptySource
    @DisplayName("확정날짜가 비어있다면 멘토링을 수락할 수 없다")
    void updateSeniorMentoringExpectedWithoutDate(String empty) throws Exception {
        Mentoring mentoring = new Mentoring(-1L, user, senior, payment, null, "topic", "question", "date1,date2,date3", 40, Status.WAITING, now(), now());
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
//        Mentoring mentoring = new Mentoring(0L, user, senior, payment, null, "topic", "question", "date1,date2,date3", 40, Status.WAITING, now(), now());
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
    @NullAndEmptySource
    @DisplayName("사유가 비어있다면 멘토링을 거절할 수 없다")
    void updateSeniorMentoringExpectedWithoutRefuse(String empty) throws Exception {
        Mentoring mentoring = new Mentoring(-1L, user, senior, payment, null, "topic", "question", "date1,date2,date3", 40, Status.WAITING, now(), now());
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
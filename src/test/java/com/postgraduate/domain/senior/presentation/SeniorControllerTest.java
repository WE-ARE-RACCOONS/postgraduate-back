package com.postgraduate.domain.senior.presentation;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.postgraduate.IntegrationTest;
import com.postgraduate.domain.available.application.dto.req.AvailableCreateRequest;
import com.postgraduate.domain.senior.application.dto.req.SeniorCertificationRequest;
import com.postgraduate.domain.senior.application.dto.req.SeniorProfileRequest;
import com.postgraduate.domain.senior.domain.entity.Info;
import com.postgraduate.domain.senior.domain.entity.Senior;
import com.postgraduate.domain.senior.domain.entity.constant.Status;
import com.postgraduate.domain.senior.domain.repository.SeniorRepository;
import com.postgraduate.domain.user.domain.entity.User;
import com.postgraduate.domain.user.domain.entity.constant.Role;
import com.postgraduate.domain.user.domain.repository.UserRepository;
import com.postgraduate.global.config.security.jwt.util.JwtUtils;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;

import java.util.List;

import static com.postgraduate.domain.senior.presentation.constant.SeniorResponseCode.SENIOR_UPDATE;
import static com.postgraduate.domain.senior.presentation.constant.SeniorResponseMessage.UPDATE_CERTIFICATION;
import static com.postgraduate.domain.senior.presentation.constant.SeniorResponseMessage.UPDATE_PROFILE;
import static java.time.LocalDateTime.now;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class SeniorControllerTest extends IntegrationTest {
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
    private String token;

    @BeforeEach
    void setUp() {
        User user = new User(0L, 1L, "mail", "후배", "011", "profile", 0, Role.SENIOR, true, now(), now(), false);
        userRepository.save(user);

        Info info = new Info("major", "서울대학교", "교수님", "키워드1,키워드2", "랩실", "인공지능", false, false, "인공지능,키워드1,키워드2");
        Senior senior = new Senior(0L, user, "certification", Status.WAITING, 0, info, null, now(), now());
        seniorRepository.save(senior);

        token = jwtUtil.generateAccessToken(user.getUserId(), Role.SENIOR);
    }

    @Test
    @DisplayName("대학원생 인증합니다")
    void updateCertification() throws Exception {
        String request = objectMapper.writeValueAsString(
                new SeniorCertificationRequest("certification")
        );
        mvc.perform(patch("/senior/certification")
                        .header(AUTHORIZATION, BEARER + token)
                        .content(request)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(SENIOR_UPDATE.getCode()))
                .andExpect(jsonPath("$.message").value(UPDATE_CERTIFICATION.getMessage()));
    }

    @Test
    @DisplayName("대학원생 프로필을 등록합니다")
    void singUpSenior() throws Exception {
        List<AvailableCreateRequest> availableCreateRequests = List.of(
                new AvailableCreateRequest("월", "17:00", "23:00"),
                new AvailableCreateRequest("금", "10:00", "20:00"),
                new AvailableCreateRequest("토", "10:00", "20:00")
        );
        String request = objectMapper.writeValueAsString(
                new SeniorProfileRequest("저는요", "한줄소개", "대상", "chatLink", availableCreateRequests)
        );

        mvc.perform(patch("/senior/profile")
                        .header(AUTHORIZATION, BEARER + token)
                        .content(request)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(SENIOR_UPDATE.getCode()))
                .andExpect(jsonPath("$.message").value(UPDATE_PROFILE.getMessage()));
    }

    @Test
    void updateAccount() {
    }

    @Test
    void getSeniorInfo() {
    }

    @Test
    void getSeniorProfile() {
    }

    @Test
    void updateSeniorProfile() {
    }

    @Test
    void getSeniorUserAccount() {
    }

    @Test
    void updateSeniorUserAccount() {
    }

    @Test
    void getSeniorDetails() {
    }

    @Test
    void testGetSeniorProfile() {
    }

    @Test
    void getSeniorTimes() {
    }

    @Test
    void getSearchSenior() {
    }

    @Test
    void getFieldSenior() {
    }

    @Test
    void checkRole() {
    }
}